import javax.print.DocFlavor;
import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The class is used to manage the affair related to the database.
 */
public class DBAffair {
    private Connection connect;
    private static int flag = 0;

    public static void setFlag(int f){
        flag = f;
    }

    public static int getFlag(){
        return flag;
    }

    public DBAffair() throws SQLException{
        connect = JDBCUtil.getConnection();
        if(flag == 1) return;
        try{
            flag = 1;
            System.out.print("Creating DB");
            ScriptRunner scriptRunner = new ScriptRunner(connect, true, true);
            Reader reader = new BufferedReader(new FileReader("FancyBankv2/sql/FancyBank.sql"));
            scriptRunner.runScript(reader);
            reader.close();
        }
        catch (Exception e){
            System.out.print(e.toString());
        }
    }

    /**
     * get all customer records from the database
     * @return an ArrayList object storing all customer objects
     * @throws SQLException
     */
    public ArrayList<Customer> getAllCustomers() throws SQLException {
        ArrayList<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM customer";
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()) {
            int customer_id = rs.getInt("customer_id");
            String customer_name = rs.getString("customer_name");
            String customer_username = rs.getString("customer_username");
            String customer_pass = rs.getString("customer_pass");

            ArrayList<Account> accounts = getOneCustomerAccounts(customer_id);
            ArrayList<Loan> loans = getOneCustomerLoans(customer_id);

            Customer customer = new Customer(customer_id, customer_name, customer_username, customer_pass, accounts, loans);
            customers.add(customer);
        }

        JDBCUtil.close(stmt, rs);
        return customers;
    }

    /**
     * get all accounts of one specific customer
     * @param customer_id
     * @return
     * @throws SQLException
     */
    public ArrayList<Account> getOneCustomerAccounts(int customer_id) throws SQLException {
        ArrayList<Account> accounts = new ArrayList<>();

        String sql = String.format("SELECT * FROM account WHERE customer_id = %d", customer_id);
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()) {
            int account_number = rs.getInt("account_number");
            String type = rs.getString("type");
            double balance = rs.getDouble("balance");
            boolean active = (rs.getInt("active") == 1);
            if(!type.equals("Security")) {
                Account account = new Account(customer_id, account_number, type, balance, active);
                accounts.add(account);
            }else{
                ArrayList<Stock> stocks = new ArrayList<>();
                sql = String.format("SELECT customer_stock.code, value, customer_stock.n_stocks " +
                        "FROM customer_stock LEFT JOIN stock " +
                        "ON customer_stock.code = stock.code " +
                        "AND customer_id = %d AND account_number = %d", customer_id, account_number);
                Statement stmt1 = connect.createStatement();
                ResultSet rs1 = stmt1.executeQuery(sql);

                while(rs1.next()) {
                    String code = rs1.getString("code");
                    double value = rs1.getDouble("value");
                    double n_stocks = rs1.getDouble("n_stocks");
                    Stock stock = new Stock(code, value, n_stocks);
                    stocks.add(stock);
                }
                JDBCUtil.close(stmt1, rs1);
                SecurityAccount securityAccount = new SecurityAccount(customer_id, account_number, type, balance, active, stocks);
                accounts.add(securityAccount);
            }
        }
        JDBCUtil.close(stmt, rs);
        return accounts;
    }

    /**
     * get all loans of one specific customer;
     * @param customer_id
     * @return
     * @throws SQLException
     */
    public ArrayList<Loan> getOneCustomerLoans(int customer_id) throws SQLException {
        ArrayList<Loan> loans = new ArrayList<>();

        String sql = String.format("SELECT * FROM loan WHERE customer_id = %d", customer_id);
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()) {
            int loan_number = rs.getInt("loan_number");
            double initial_amount = rs.getDouble("initial_amount");
            double balance = rs.getDouble("balance");
            double interest_rate = rs.getDouble("interest_rate");
            int duration = rs.getInt("duration");
            String collateral_name = rs.getString("collateral_name");
            double collateral_amount = rs.getDouble("collateral_amount");
            String status = rs.getString("status");

            Loan loan = new Loan(customer_id, loan_number, initial_amount, balance, interest_rate, duration, collateral_name, collateral_amount, status);
            loans.add(loan);
        }
        JDBCUtil.close(stmt, rs);
        return loans;
    }

    public void updateCustomer(Customer customer) throws SQLException {
        // find whether the customer exists in the database
        int customer_id = customer.getCustomer_id();
        String sql = String.format("SELECT * FROM customer WHERE customer_id = %d", customer_id);
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        if(!rs.next()) {
            String customer_name = customer.getCustomer_name();
            String customer_username = customer.getCustomer_username();
            String customer_pass = customer.getCustomer_pass();
            sql = String.format("INSERT INTO customer(customer_id, customer_name, customer_username, customer_pass) VALUES(%d, '%s', '%s', '%s')", customer_id, customer_name, customer_username, customer_pass);
            stmt.execute(sql);
        }

        ArrayList<Account> accounts = customer.getAccounts();
        for(int i = 0; i < accounts.size(); i++) {
            updateAccount(accounts.get(i));
        }

        ArrayList<Loan> loans = customer.getLoans();
        for(int i = 0; i < loans.size(); i++) {
            updateLoan(loans.get(i));
        }
        JDBCUtil.close(stmt, rs);
    }


    public void updateAccount(Account account) throws SQLException {
        // check whether the account exists in the database
        int customer_id = account.getCustomer_id();
        int account_number= account.getAccount_number();
        String type = account.getType();
        double balance = account.getBalance();
        int active = (account.isActive()) ? 1 : 0;

        String sql = String.format("SELECT customer_id FROM account WHERE customer_id = %d AND account_number = %d", customer_id, account_number);

        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        if(!rs.next()) { // the account doesn't exist in the database, insert the information of the account into the database
            sql = String.format("INSERT INTO account(customer_id, account_number, type, balance, active) VALUES(%d, %d, '%s', %f, %d)", customer_id, account_number, type, balance, active);
            stmt.execute(sql);

            if(type.equals("Security")) {
                SecurityAccount securityAccount = (SecurityAccount)account;
                ArrayList<Stock> stocks = securityAccount.getStocks();
                for(Stock stock: stocks) {
                    String code = stock.getCode();
                    double n_stocks = stock.getN_stocks();
                    sql = String.format("INSERT INTO customer_stock(customer_id, account_number, code, n_stocks) VALUES(%d, %d, '%s', %f)", customer_id, account_number, code, n_stocks);
                    stmt.execute(sql);
                }
            }
        }else{ // the account does exist in the database, just update
            sql = String.format("UPDATE account SET balance = %f WHERE customer_id = %d AND account_number = %d", balance, customer_id, account_number);
            stmt.execute(sql);

            if(type.equals("Security")) {
                SecurityAccount securityAccount = (SecurityAccount)account;
                ArrayList<Stock> stocks = securityAccount.getStocks();
                for(Stock stock: stocks) {
                    String code = stock.getCode();
//                    double value = stock.getValue();
                    double n_stocks = stock.getN_stocks();
                    // check whether the stock of the customer in the database
                    sql = String.format("SELECT * FROM customer_stock WHERE customer_id = %d AND account_number = %d AND code = '%s'", customer_id, account_number, code);
                    rs = stmt.executeQuery(sql);
                    if(!rs.next()) { // insert
                        sql = String.format("INSERT INTO customer_stock(customer_id, account_number, code, n_stocks) VALUES(%d, %d, '%s', %f)", customer_id, account_number, code, n_stocks);
                    }else{ // update
                        sql = String.format("UPDATE customer_stock SET n_stocks = %f WHERE customer_id = %d AND account_number = %d AND code = '%s'", n_stocks, customer_id, account_number, code);
                    }
                    stmt.execute(sql);
                }
            }
        }
        JDBCUtil.close(stmt, rs);
    }

    public void updateLoan(Loan loan) throws SQLException {
        // check whether the loan exists in the database;
        int customer_id = loan.getCustomer_id();
        int loan_number = loan.getLoan_number();
        double initial_amount = loan.getInitial_amount();
        double balance = loan.getBalance();
        double interest_rate = loan.getInterest_rate();
        int duration = loan.getDuration();
        String collateral_name = loan.getCollateral_name();
        double collateral_amount = loan.getCollateral_amount();
        String status = loan.getStatus();

        String sql = String.format("SELECT * FROM loan WHERE customer_id = %d AND loan_number = %d", customer_id, loan_number);
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        if(!rs.next()) { // the loan doesn't exist in the database, insert
            sql = String.format("INSERT INTO loan(customer_id, loan_number, initial_amount, balance, interest_rate, duration, collateral_name, collateral_amount, status)" +
                            "VALUES(%d, %d, %f, %f, %f, %d, '%s', %f, '%s')",
                    customer_id, loan_number, initial_amount, balance, interest_rate, duration, collateral_name, collateral_amount, status);
        }else{ // the loan does exist in the database, update
            sql = String.format("UPDATE loan SET balance = %f, status = '%s' WHERE customer_id = %d AND loan_number = %d", balance, status, customer_id, loan_number);
        }
        stmt.execute(sql);
        JDBCUtil.close(stmt, rs);
    }

    /**
     * get all stock records from the database
     * @return an ArrayList object storing all stock objects
     */
    public ArrayList<Stock> getAllStocks() throws SQLException {
        ArrayList<Stock> stocks = new ArrayList<>();

        String sql = "SELECT * FROM stock";
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()) {
            String code = rs.getString("code");
            double value = rs.getDouble("value");
            double n_stocks = rs.getDouble("n_stocks");
            Stock stock = new Stock(code, value, n_stocks);
            stocks.add(stock);
        }
        JDBCUtil.close(stmt, rs);
        return stocks;
    }

    public void updateStock(Stock stock) throws SQLException {
        // check whether the stock exists in the database;
        String code = stock.getCode();
        double value = stock.getValue();
        double n_stocks = stock.getN_stocks();

        String sql = String.format("SELECT * FROM stock WHERE code = '%s'", code);

        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        if(!rs.next()) { // the stock doesn't exist in the database, insert
            sql = String.format("INSERT INTO stock(code, value, n_stocks) VALUES('%s', %f, %f)", code, value, n_stocks);
        }else{
            sql = String.format("UPDATE stock SET value = %f, n_stocks = %f WHERE code = '%s'", value, n_stocks, code);
        }
        stmt.execute(sql);
        JDBCUtil.close(stmt, rs);
    }

    /**
     * get all transaction records from the database
     * @return an arrayList object storing all transaction objects
     * @throws SQLException
     */
    public ArrayList<Transaction> getAllTransaction() throws SQLException {
        ArrayList<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM transaction";
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()) {
            int transaction_id = rs.getInt("transaction_id");
            String transaction_type = rs.getString("transaction_type");
            int receiving_account_id = rs.getInt("receiving_account_id");
            int receiving_user_id = rs.getInt("receiving_user_id");
            int sending_account_id = rs.getInt("sending_account_id");
            int sending_user_id = rs.getInt("sending_user_id");
            double amount = rs.getDouble("amount");
            String code = rs.getString("code");
            Double n_stock = rs.getDouble("n_stock");
            if(transaction_type.equals("general")) {
                Transaction transaction = new Transaction(transaction_id, transaction_type, receiving_account_id, receiving_user_id, sending_account_id, sending_user_id, amount);
                transactions.add(transaction);
            }else{
                StockTransaction stockTransaction = new StockTransaction(transaction_id, transaction_type, receiving_account_id, receiving_account_id, sending_account_id, sending_user_id, amount, code, n_stock);
                transactions.add(stockTransaction);
            }
        }
        JDBCUtil.close(stmt, rs);
        return transactions;
    }

    public void updateTransaction(Transaction transaction) throws SQLException {
        int transaction_id = transaction.getTransaction_id();
        String sql = String.format("SELECT * FROM transaction WHERE transaction_id = %d", transaction_id);

        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        if(!rs.next()) {
            String transaction_type = transaction.getTransaction_type();
            int receiving_account_id = transaction.getReceiving_account_id();
            int receiving_user_id = transaction.getReceiving_user_id();
            int sending_account_id = transaction.getSending_account_id();
            int sending_user_id = transaction.getSending_user_id();
            double amount = transaction.getAmount();
            if(transaction_type.equals("general")) {
                sql = String.format("INSERT INTO transaction(transaction_id, transaction_type, receiving_account_id, receiving_user_id, sending_account_id, sending_user_id, amount)" +
                        "VALUES(%d, '%s', %d, %d, %d, %d, %f)", transaction_id, transaction_type, receiving_account_id, receiving_user_id, sending_account_id, sending_user_id, amount);
            }else{
                StockTransaction stockTransaction = (StockTransaction)transaction;
                String code = stockTransaction.getCode();
                double n_stock = stockTransaction.getN_stock();
                sql = String.format("INSERT INTO transaction(transaction_id, transaction_type, receiving_account_id, receiving_user_id, sending_account_id, sending_user_id, amount, code, n_stock)" +
                        "VALUES(%d, '%s', %d, %d, %d, %d, %f, '%s', %f)", transaction_id, transaction_type, receiving_account_id, receiving_user_id, sending_account_id, sending_user_id, amount, code, n_stock);
            }
            stmt.execute(sql);
            JDBCUtil.close(stmt, rs);
        }
    }

    public void update(Customer customer, ArrayList<Transaction>transactions, ArrayList<Stock>stocks) throws SQLException {
        updateCustomer(customer);
        for(Transaction transaction: transactions) {
            updateTransaction(transaction);
        }

        for(Stock stock: stocks) {
            updateStock(stock);
        }
    }

    public void closeDB() throws SQLException {
        JDBCUtil.close(connect);
    }
}

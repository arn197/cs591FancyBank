import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class BankSystem {

    private static Bank bank;
    private static BankDisplay bankDisplay;

    public static void home_interface(){
        bankDisplay.welcome();
    }

    public static void login_interface(int flag){
        bankDisplay.login(flag);
    }

    public static void register_interface(){
        ArrayList<String> account_types = new ArrayList<>(bank.getAccount_types());
        account_types.remove("Security");
        bankDisplay.register(account_types);
    }

    public static void loan_interface(){
        bankDisplay.loan(bank.getCustomers().get(bank.getCurrent_user()));
    }

    public static void updateSettings(Double[] new_values){
        bank.updateSettings(new_values);
    }

    public static void manager_interface(){
        bankDisplay.manager(bank.getManagers().get(bank.getCurrent_user()), bank.getCustomers(), bank.getTransactions(), bank.getLoans());
    }

    public static void customer_interface(int flag){
        if(flag != -1) bankDisplay.customer(bank.getCustomers().get(flag), bank.customerTransactions(flag), 0);
        else bankDisplay.customer(bank.getCustomers().get(bank.getCurrent_user()), bank.customerTransactions(bank.getCurrent_user()), 1);
    }

    public static boolean login(String user_name, String pass, int flag){
        if(flag != 1){
            Manager manager = bank.manager_login(user_name, pass);
            if(manager == null) return false;
            bankDisplay.manager(manager, bank.getCustomers(), bank.getTransactions(), bank.getLoans());
        }
        else{
            Customer customer = bank.login(user_name, pass);
            if(customer == null) return false;
            bankDisplay.customer(customer, bank.customerTransactions(bank.getCurrent_user()), 1);
            return true;
        }
        return false;
    }

    public static void requestLoan(double amount, int duration, String collateral_name, double collateral_amount){
        bank.requestLoan(amount, duration, collateral_name, collateral_amount);
    }

    public static String registerNewUser(String customer_name, String username, String password, double balance, String type){
        return bank.newCustomer(customer_name, username, password, balance, type);
    }

    public static String newAccount(double balance, String type){
        return bank.newAccount(balance, type);
    }

    public static boolean buyStock(double amount, int account_number, int stock_code, int paying){
        return bank.buyStock(amount, account_number, stock_code, paying);
    }

    public static boolean sellStock(double amount, int account_number, int stock_code, int paying){
        return bank.sellStock(amount, account_number, stock_code, paying);
    }

    public static boolean withdraw(double amount, int account_number){
        return bank.withdraw(amount, account_number);
    }

    public static boolean deposit(double amount, int account_number){
        return bank.deposit(amount, account_number);
    }

    public static void viewAccount(Account account){
        if(account.getType().equals("Security")) bankDisplay.viewAccount((SecurityAccount) account, bank.getSecurityTransactionsAccount(account), bank.getCustomers().get(bank.getCurrent_user()), bank.getAvailable_stocks(), bank.getCustomers().get(bank.getCurrent_user()).getAccounts());
        else bankDisplay.viewAccount(account,bank.getTransactionsAccount(account),bank.getCustomers().get(bank.getCurrent_user()));
    }

    public static boolean payLoan(double amount, int account_selection, Loan loan){
        return bank.payLoan(amount, loan, account_selection);
    }

    public static String[] currencies(){
        return bank.getCurrencies();
    }

    public static double currency_rate(int pos, double amount) {
        return bank.getConversion_rates()[pos] * amount;
    }

    public static boolean transfer(int s, int d, double amount){
        return bank.transfer(s, d, amount);
    }

    public static void viewLoan(Customer customer, Loan loan, double interest_rate){
        bankDisplay.viewLoan(customer, loan, interest_rate);
    }

    public static int closeAccount(Customer customer, int pos){
        return bank.closeAccount(customer, pos);
    }

    public static boolean addStock(String code, Double value, Double number){
        Stock new_stock = new Stock(code, value, number);
        return bank.addAvailable_stocks(new_stock);
    }

    public static void deleteStock(int n){
        boolean isHold = false;
        boolean chold;
        for(Customer customer:bank.getCustomers()){
            chold = customer.checkIfHoldStock(bank.getAvailable_stocks().get(n).getCode());
            isHold = isHold || chold;
        }
        if(!isHold) bank.deleteStock(n);
    }

    public static void editStock(int n, Double new_value, Double new_number){
        bank.editStock(n, new_value, new_number);
        for(Customer customer:bank.getCustomers()) {
            customer.editHoldStock(bank.getAvailable_stocks().get(n).getCode(), new_value);
        }
    }



    public static void refreshStockMarket(){
        bankDisplay.setStock(bank.getAvailable_stocks());
    }

    public static void tableButtonPress(String command, JTable jTable){
        switch(command){
            case "View Account":
                int pos = jTable.getSelectedRow();
                Customer customer = bank.getCustomers().get(bank.getCurrent_user());
                Account account = customer.getAccounts().get(pos);
                viewAccount(account);
                break;
            case "Close Account":
                pos = jTable.getSelectedRow();
                customer = bank.getCustomers().get(bank.getCurrent_user());
                int res = closeAccount(customer, pos);
                if(res == -1){
                    bankDisplay.alert("You don't have enough balance to close this account");
                }
                else if(res == -2){
                    bankDisplay.alert("You have active stocks in this account. Sell them first");
                }
                else{
                    bankDisplay.alert("Your remaining balance will be sent by check to your registered address");
                    customer_interface(-1);
                }
                break;
            case "Change Currency":
                pos = jTable.getSelectedRow();
            case "Request Loan":
                loan_interface();
                break;
            case "View Loan":
                customer = bank.getCustomers().get(bank.getCurrent_user());
                Loan loan = customer.getLoans().get(jTable.getSelectedRow());
                viewLoan(customer, loan, bank.getLoan_interest_rate());
                break;
            case "View Customer":
                pos = jTable.getSelectedRow();
                customer = bank.getCustomers().get(pos);
                bankDisplay.customer(customer, bank.customerTransactions(pos), 0);
                break;
            case "Pay Interest":
                bank.payInterest();
                bankDisplay.manager(bank.getManagers().get(bank.getCurrent_user()), bank.getCustomers(), bank.getTransactions(), bank.getLoans());
                break;
            case "Get Report":
                ArrayList<Transaction> transactions = bank.getNew_transactions();
                bankDisplay.report(transactions);
                break;
            case "Approve Loan":
                pos = jTable.getSelectedRow();
                bank.loanStatusChange("Active", pos);
                manager_interface();
                break;
            case "Decline Loan":
                pos = jTable.getSelectedRow();
                bank.loanStatusChange("Denied", pos);
                manager_interface();
                break;
            case "Delete":
                pos = jTable.getSelectedRow();
                deleteStock(pos);
                refreshStockMarket();
                break;
            case "Edit":
                pos = jTable.getSelectedRow();
                Double old_value = bank.getAvailable_stocks().get(pos).getValue();
                Double old_number = bank.getAvailable_stocks().get(pos).getN_stocks();
                bankDisplay.managerSetStock(old_value, old_number, pos);
                break;
        }
    }

    public static void buttonPress(String command) throws SQLException {
        switch(command){
            case "Transfer":
                Customer customer = bank.getCustomers().get(bank.getCurrent_user());
                bankDisplay.transfer(customer);
                break;
            case "New Account":
                bankDisplay.newAccount(bank.getAccount_types(), bank.getCustomers().get(bank.getCurrent_user()));
                break;
            case "Logout":
                bank.logout();
                bankDisplay.welcome();
                break;
            case "Back":
                if(bank.getManager_online() == 1) bankDisplay.manager(bank.getManagers().get(bank.getCurrent_user()), bank.getCustomers(), bank.getTransactions(), bank.getLoans());
                else bankDisplay.customer(bank.getCustomers().get(bank.getCurrent_user()), bank.customerTransactions(bank.getCurrent_user()), -1);
                break;
            case "Bank Settings":
                bankDisplay.managerSettings(bank.getMinimum_balance(), bank.getInterest_rate(), bank.getLoan_interest_rate(), bank.getService_charge(), bank.getInterest_balance());
                break;
            case "Stock Market":
                bankDisplay.setStock(bank.getAvailable_stocks());
        }
    }

    public static void start() throws SQLException {
        DBAffair dbAffair = new DBAffair();
        HashMap<String, String> settings = dbAffair.getSettings();
        String bank_name = settings.get("bank_name");
        int starting_balance = Integer.parseInt(settings.get("starting_bal"));
        int minimum_balance = Integer.parseInt(settings.get("min_bal"));
        int min_security_balance = Integer.parseInt(settings.get("min_security_bal"));
        String manager_name = settings.get("manager_username");
        String manager_pass = settings.get("manager_pass");
        double service_charge = Double.parseDouble(settings.get("service_charge"));
        double interest_rate = Double.parseDouble(settings.get("interest"));
        double loan_interest_rate = Double.parseDouble(settings.get("loan_interest"));
        double high_interest_balance = Double.parseDouble(settings.get("high_interest_bal"));
        double trading_fees = Double.parseDouble(settings.get("trading_fees"));

        String[] currencies = new String[]{"USD","GBP","INR"};
        Double[] conversion_rates = new Double[]{1.0, 1.30, 0.014};
        ArrayList<String> account_types = new ArrayList<>();
        account_types.add("Checking");
        account_types.add("Savings");
        account_types.add("Security");

        ArrayList<Stock> available_stocks = dbAffair.getAllStocks();
        dbAffair.closeDB();

        bank = new Bank(bank_name, starting_balance, minimum_balance, manager_name, manager_pass, service_charge, interest_rate, loan_interest_rate, high_interest_balance, currencies, conversion_rates, account_types, min_security_balance, available_stocks, trading_fees);
//         bank.newCustomer("p","p","123",10000,"Checking");
//         bank.setCurrent_user(0);
        //bank.newCustomer("Aaron", "arn197", "123", 10000, "Checking");
        bank.setCurrent_user(0);
        bankDisplay = new BankDisplay(bank, 1280, 720);
        customer_interface(-1);
    }
}

import java.util.ArrayList;

public class Customer {
    private int customer_id;
    private String customer_name;
    private String customer_username;
    private String customer_pass;
    private ArrayList<Account> accounts;
    private ArrayList<Loan> loans;

    public ArrayList<Loan> getLoans() {
        return loans;
    }

    public String getCustomer_username() {
        return customer_username;
    }

    Customer(String customer_name, String customer_username, String customer_pass){
        this.customer_name = customer_name;
        this.customer_pass = customer_pass;
        this.customer_username = customer_username;
        accounts = new ArrayList<>();
        loans = new ArrayList<>();
    }

    Customer(int customer_id, String customer_name, String customer_username, String customer_pass, ArrayList<Account> accounts, ArrayList<Loan>loans) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_username = customer_username;
        this.customer_pass = customer_pass;
        this.accounts = accounts;
        this.loans = loans;
    }

    public void requestLoan(int duration, double amount, String collateral_name, double collateral_amount, double interest_rate){
        loans.add(new Loan(duration, amount, this.customer_id, loans.size()+1, collateral_name, collateral_amount, interest_rate));
    }

    public boolean buyStock(double amount, int account_number, Stock stock, int paying_account_number, double fees){
        SecurityAccount securityAccount = null;
        Account paying = null;
        for(Account account: accounts){
            if(account_number == account.getAccount_number()){
                securityAccount = (SecurityAccount) account;
            }
            if(paying_account_number == account.getAccount_number()){
                paying = account;
            }
        }
        if(paying == null || securityAccount == null) return false;
        if(stock.getValue() * amount + fees >= paying.getBalance()) return false;
        int flag = 0;
        for(Stock stock1: securityAccount.getStocks()){
            if(stock1.getCode().equals(stock.getCode())){
                stock1.setN_stocks(stock1.getN_stocks() + amount);
                flag = 1;
            }
        }
        if(flag == 0) securityAccount.addStock(new Stock(stock.getCode(), stock.getValue(), amount));
        paying.setBalance(paying.getBalance() - stock.getValue() * amount - fees);
        return true;
    }

    public boolean checkIfHoldStock(String stock_code){
        boolean hold = false;
        for(Account account: accounts) {
            String acc_type = account.getType();
            if(acc_type.equals("Security")){
                SecurityAccount s_account = (SecurityAccount) account;
                for(Stock stock1: s_account.getStocks()){
                    if(stock1.getCode().equals(stock_code)) hold = true;
                }
            }
        }
        return hold;
    }

    public void editHoldStock(String stock_code, double new_value){
        for(Account account: accounts) {
            String acc_type = account.getType();
            if(acc_type.equals("Security")){
                SecurityAccount s_account = (SecurityAccount) account;
                for(Stock stock1: s_account.getStocks()){
                    if(stock1.getCode().equals(stock_code)) stock1.setValue(new_value);
                }
            }
        }
    }


    public String getStockCode(int account_number, int stock_num){
        SecurityAccount securityAccount = null;
        for(Account account: accounts) {
            if (account_number == account.getAccount_number()) {
                securityAccount = (SecurityAccount) account;
            }
        }
        Stock stock = securityAccount.getStocks().get(stock_num);
        return stock.getCode();
    }

    public boolean sellStock(double amount, int account_number, Stock stock, int paying_account_number, double fees){
        SecurityAccount securityAccount = null;
        Account paying = null;
        for(Account account: accounts){
            if(account_number == account.getAccount_number()){
                securityAccount = (SecurityAccount) account;
            }
            if(paying_account_number == account.getAccount_number()){
                paying = account;
            }
        }
        if(paying == null || securityAccount == null) return false;
        if(paying.getBalance() - fees + stock.getValue() * amount <= 0) return false;
        Stock s = null;
        int flag = 0;

        for(Stock stock1: securityAccount.getStocks()){
            if(stock1.getCode().equals(stock.getCode()) && stock1.getN_stocks() >= amount){
                stock1.setN_stocks(stock1.getN_stocks() - amount);
                if(stock1.getN_stocks() == 0){
                    s = stock1;
                }
                flag = 1;
            }
        }
//        Stock stock1 = securityAccount.getStocks().get(stock_num);
//        if(stock1.getN_stocks() >= amount){
//            stock1.setN_stocks(stock1.getN_stocks() - amount);
//            if(stock1.getN_stocks() == 0){
//                s = stock1;
//            }
//            flag = 1;
//        }

        if(flag == 0) return false;

        if(s != null){
            securityAccount.removeStocks(s);
        }
        paying.setBalance(paying.getBalance() + stock.getValue() * amount - fees);
        return true;
    }

    public int withdraw(double amount, int account_number, double service_charge){
        for(Account account: accounts){
            if(account_number == account.getAccount_number()){
                double bal = account.getBalance();
                if(account.getType().equals("Checking")) bal -= service_charge;
                if(bal - amount < 0) return -1;
                account.setBalance(bal - amount);
                if(account.getType().equals("Checking")) return 1;
                return 0;
            }
        }
        return -1;
    }

    public boolean deposit(double amount, int account_number){
        for(Account account: accounts){
            if(account_number == account.getAccount_number()){
                double bal = account.getBalance();
                account.setBalance(bal + amount);
                return true;
            }
        }
        return false;
    }

    public String getCustomer_pass() {
        return customer_pass;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public ArrayList<Account> getAccounts() {
        ArrayList<Account> active_accounts = new ArrayList<>();
        for(Account account: accounts){
            if(account.isActive()) active_accounts.add(account);
        }
        return active_accounts;
    }

    public void addAccount(Account account){
        this.accounts.add(account);
    }
}

import java.util.ArrayList;

public class Customer {
    private String customer_name;
    private String customer_username;
    private int customer_id;
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

    public void requestLoan(int duration, double amount, String collateral_name, double collateral_amount, double interest_rate){
        loans.add(new Loan(duration, amount, this.customer_id, collateral_name, collateral_amount, interest_rate));
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

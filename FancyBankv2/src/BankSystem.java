import javax.swing.*;
import java.util.ArrayList;

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

    public static boolean closeAccount(Customer customer, int pos){
        return bank.closeAccount(customer, pos);
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
                if(!closeAccount(customer, pos)){
                    bankDisplay.alert("You don't have enough balance to close this account");
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
        }
    }

    public static void buttonPress(String command){
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
        }
    }

    public static void start(){
        String bank_name = "The Bank";
        int starting_balance = 1000000;
        int minimum_balance = 100;
        int min_security_balance = 10000;
        String manager_name = "manager";
        String manager_pass = "123";
        double service_charge = 15.0;
        double interest_rate = 8.0;
        double loan_interest_rate = 8.0;
        double high_interest_balance = 10000;
        String[] currencies = new String[]{"USD","GBP","INR"};
        Double[] conversion_rates = new Double[]{1.0, 1.30, 0.014};
        ArrayList<String> account_types = new ArrayList<>();
        account_types.add("Checking");
        account_types.add("Savings");
        account_types.add("Security");

        ArrayList<Stock> available_stocks = new ArrayList<>();
        available_stocks.add(new Stock("aapl",200,1000));
        available_stocks.add(new Stock("googl",200,1000));

        bank = new Bank(bank_name, starting_balance, minimum_balance, manager_name, manager_pass, service_charge, interest_rate, loan_interest_rate, high_interest_balance, currencies, conversion_rates, account_types, min_security_balance, available_stocks);
        bank.newCustomer("Aaron","arn197","123",10000,"Checking");
        bank.setCurrent_user(0);
        bankDisplay = new BankDisplay(bank, 1280, 720);
        customer_interface(-1);
    }
}

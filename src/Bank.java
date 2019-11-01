import java.util.ArrayList;

public class Bank {
    private String bank_name;
    private ArrayList<Manager> managers;
    private ArrayList<Customer> customers;
    private ArrayList<Transaction> transactions;
    private int accountCount = 1;
    private ArrayList<String> account_types;
    private double minimum_balance;
    private String[] currencies;
    private Double[] conversion_rates;
    private int current_user = -1;
    private double service_charge = 0;
    private double interest_balance;
    private double interest_rate;
    private double loan_interest_rate;
    private int manager_online = 0;
    private Account mainAccount;
    private ArrayList<Transaction> new_transactions;

    public Double[] getConversion_rates() {
        return conversion_rates;
    }

    public void setConversion_rates(Double[] conversion_rates) {
        this.conversion_rates = conversion_rates;
    }

    public int getManager_online() {
        return manager_online;
    }

    public void setManager_online(int manager_online) {
        this.manager_online = manager_online;
    }

    public ArrayList<String> getAccount_types() {
        return account_types;
    }

    public String[] getCurrencies(){
        return currencies;
    }

    public void updateSettings(Double[] new_values){
        this.minimum_balance = new_values[0];
        this.interest_rate = new_values[1];
        this.loan_interest_rate = new_values[2];
        this.service_charge = new_values[3];
        this.interest_balance = new_values[4];
    }

    public void chargeService(int account_number){
        transactions.add(new Transaction(-2,-2,account_number, current_user, service_charge));
        mainAccount.setBalance(mainAccount.getBalance() + service_charge);
    }

    public boolean closeAccount(Customer customer, int pos){
        Account account = customer.getAccounts().get(pos);
        if(account.getType().equals("Checking")){
            if(account.getBalance() < service_charge) return false;
            chargeService(account.getAccount_number());
        }
        account.setActive(false);
        return true;
    }

    public void setAccount_types(ArrayList<String> account_types) {
        this.account_types = account_types;
    }

    public ArrayList<Transaction> getTransactionsAccount(Account account){
        ArrayList<Transaction> t = new ArrayList<>();
        for(Transaction transaction: this.transactions){
            if(transaction.getReceiving_account_id() == account.getAccount_number() || transaction.getSending_account_id() == account.getAccount_number()){
                t.add(transaction);
            }
        }
        return t;
    }

    public ArrayList<Transaction> getNew_transactions(){
        ArrayList<Transaction> transactions = new ArrayList<>(new_transactions);
        new_transactions.clear();
        return transactions;
    }

    public ArrayList<Loan> getLoans(){
        ArrayList<Loan> loans = new ArrayList<>();
        for(Customer customer: customers){
            loans.addAll(customer.getLoans());
        }
        return loans;
    }

    public void loanStatusChange(String status, int pos){
        Loan loan = getLoans().get(pos);
        loan.setStatus(status);
    }

    public void payInterest(){
        double total = 0;
        for(Customer customer: customers){
            for(Account account: customer.getAccounts()){
                if(account.getBalance() >= this.interest_balance){
                    double amt = (this.interest_rate/100.0) * account.getBalance();
                    transactions.add(new Transaction(account.getAccount_number(), customer.getCustomer_id(), -2, -2, amt));
                    customer.deposit(amt, account.getAccount_number());
                    total += amt;
                    new_transactions.add(transactions.get(transactions.size() -1 ));
                }
            }
        }
        mainAccount.setBalance(mainAccount.getBalance() - total);
    }

    public boolean transfer(int s, int d, double amount){
        Account sending = customers.get(current_user).getAccounts().get(s);
        Account receiving = customers.get(current_user).getAccounts().get(d);
        if(withdraw(amount, sending.getAccount_number())){
            if(sending.getType().equals("Checking")){
                chargeService(sending.getAccount_number());
            }
            deposit(amount, receiving.getAccount_number());
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount, int account_number){
        int flag = customers.get(current_user).withdraw(amount, account_number, service_charge);
        if(flag >= 0){
            transactions.add(new Transaction(-1,-1,account_number, current_user, amount));
            new_transactions.add(transactions.get(transactions.size() -1 ));
            if(flag == 1) {
                chargeService(account_number);
                new_transactions.add(transactions.get(transactions.size() -1 ));
            }
            return true;
        }
        return false;
    }

    public boolean deposit(double amount, int account_number){
        if(customers.get(current_user).deposit(amount, account_number)){
            transactions.add(new Transaction(account_number,current_user,-1, -1, amount));
            new_transactions.add(transactions.get(transactions.size() -1 ));
            return true;
        }
        return false;
    }

    public void requestLoan(double amount, int duration, String collateral_name, double collateral_amount){
        customers.get(current_user).requestLoan(duration, amount, collateral_name, collateral_amount, this.loan_interest_rate);
    }

    public boolean payLoan(double amount, Loan loan, int account_selection){
        if(withdraw(amount, customers.get(current_user).getAccounts().get(account_selection).getAccount_number())){
            loan.setBalance(loan.getBalance() - amount);
            if(loan.getBalance() == 0){
                loan.setStatus("Paid");
            }
            return true;
        }
        return false;
    }

    public double getService_charge() {
        return service_charge;
    }

    public void setService_charge(double service_charge) {
        this.service_charge = service_charge;
    }

    public double getInterest_balance() {
        return interest_balance;
    }

    public void setInterest_balance(double interest_balance) {
        this.interest_balance = interest_balance;
    }

    public double getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(double interest_rate) {
        this.interest_rate = interest_rate;
    }

    public double getLoan_interest_rate() {
        return loan_interest_rate;
    }

    public void setLoan_interest_rate(double loan_interest_rate) {
        this.loan_interest_rate = loan_interest_rate;
    }

    public ArrayList<Transaction> customerTransactions(int id){
        Customer customer = customers.get(id);
        ArrayList<Transaction> trs = new ArrayList<>();
        for(Transaction transaction: this.transactions){
            if(transaction.getReceiving_user_id() == customer.getCustomer_id() || transaction.getSending_user_id() == customer.getCustomer_id()){
                trs.add(transaction);
            }
        }
        return trs;
    }

    public double getMinimum_balance() {
        return minimum_balance;
    }

    public void setMinimum_balance(double minimum_balance) {
        this.minimum_balance = minimum_balance;
    }

    Bank(String name, int starting_balance, int minimum_balance, String manager_name, String pass, double service_charge, double interest_rate, double loan_interest_rate, double interest_balance, String[] currency, Double[] conversion_rates){
        customers = new ArrayList<>();
        managers = new ArrayList<>();
        transactions = new ArrayList<>();
        account_types = new ArrayList<>();
        new_transactions = new ArrayList<>();
        account_types.add("Checking");
        account_types.add("Savings");
        this.currencies = new String[]{"USD","GBP","INR"};
        this.conversion_rates = new Double[]{1.0, 1.30, 0.014};
        this.minimum_balance = minimum_balance;
        this.interest_balance = interest_balance;
        this.interest_rate = interest_rate;
        this.loan_interest_rate = loan_interest_rate;
        this.bank_name = name;
        this.currencies = currency;
        this.conversion_rates = conversion_rates;
        this.managers.add(new Manager(manager_name, pass));
        this.mainAccount = new Account(0, -1, "Checking", starting_balance);
        this.service_charge = service_charge;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public ArrayList<Manager> getManagers() {
        return managers;
    }

    public void setManagers(ArrayList<Manager> managers) {
        this.managers = managers;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String newCustomer(String customer_name, String customer_username, String customer_pass, double balance, String type){
        for (Customer customer : this.customers) {
            if (customer.getCustomer_username().equals(customer_username)) {
                return "That username is already taken";
            }
        }
        if(balance < minimum_balance) return "The minimum balance is " + minimum_balance;
        Customer customer = new Customer(customer_name, customer_username, customer_pass);
        customer.setCustomer_id(customers.size());
        customers.add(customer);
        addAccount(customer.getCustomer_id(), balance, type);
        return "success";
    }


    public void addAccount(int customer_id, double balance, String type){
        Account account = new Account(this.accountCount, customer_id, type, balance);
        this.transactions.add(new Transaction(this.accountCount, customer_id, -1, -1, balance));
        new_transactions.add(transactions.get(transactions.size() -1 ));
        if(type.equals("Checking")) chargeService(account.getAccount_number());
        this.accountCount += 1;
        this.customers.get(customer_id).addAccount(account);
    }

    public int getAccountCount() {
        return accountCount;
    }

    public void setAccountCount(int accountCount) {
        this.accountCount = accountCount;
    }

    public void logout(){
        this.manager_online = 0;
        current_user = -1;
    }

    public String newAccount(double balance, String type){
        if(balance < minimum_balance) return "Minimum balance is " + minimum_balance;
        if(current_user == -1) return "User not registered";
        addAccount(current_user, balance, type);
        return "success";
    }

    public int getCurrent_user() {
        return current_user;
    }

    public void setCurrent_user(int current_user) {
        this.current_user = current_user;
    }

    public Customer login(String username, String pass){
        for(Customer customer: this.customers){
            if(customer.getCustomer_username().equals(username) && customer.getCustomer_pass().equals(pass)){
                current_user = customer.getCustomer_id();
                return customer;
            }
        }
        return null;
    }

    public Manager manager_login(String username, String pass){
        for(Manager manager: this.managers){
            if(manager.getManager_name().equals(username) && manager.getPass().equals(pass)){
                current_user = manager.getManager_id();
                this.manager_online = 1;

                return manager;
            }
        }
        return null;
    }

}

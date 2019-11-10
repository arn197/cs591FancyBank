public class Loan {
    private int customer_id;
    private int loan_number;
    private double initial_amount;
    private double balance;
    private double interest_rate;
    private int duration;
    private String collateral_name;
    private double collateral_amount;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Loan(int duration, double amount, int customer_id, int loan_number, String collateral_name, double collateral_amount, double interest_rate){
        this.duration = duration;
        this.initial_amount = amount;
        this.balance = amount;
        this.collateral_name = collateral_name;
        this.collateral_amount = collateral_amount;
        this.customer_id = customer_id;
        this.loan_number = loan_number;
        this.interest_rate = interest_rate;
        this.status = "Pending";
    }

    // initialize from database
    public Loan(int customer_id,
                int loan_number,
                double initial_amount,
                double balance,
                double interest_rate,
                int duration,
                String collateral_name,
                double collateral_amount,
                String status) {
        this.customer_id = customer_id;
        this.loan_number = loan_number;
        this.initial_amount = initial_amount;
        this.balance = balance;
        this.interest_rate = interest_rate;
        this.duration = duration;
        this.collateral_name = collateral_name;
        this.collateral_amount = collateral_amount;
        this.status = status;
    }


    public void approve(){
        this.status = "Active";
    }

    public void decline(){
        this.status = "Denied";
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public double getInitial_amount() {
        return initial_amount;
    }

    public void setInitial_amount(double initial_amount) {
        this.initial_amount = initial_amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(double interest_rate) {
        this.interest_rate = interest_rate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCollateral_name() {
        return collateral_name;
    }

    public void setCollateral_name(String collateral_name) {
        this.collateral_name = collateral_name;
    }

    public double getCollateral_amount() {
        return collateral_amount;
    }

    public void setCollateral_amount(double collateral_amount) {
        this.collateral_amount = collateral_amount;
    }

    public int getLoan_number() {
        return loan_number;
    }
}

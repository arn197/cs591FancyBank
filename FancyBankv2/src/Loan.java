public class Loan {
    private int customer_id;
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

    Loan(int duration, double amount, int customer_id, String collateral_name, double collateral_amount, double interest_rate){
        this.duration = duration;
        this.initial_amount = amount;
        this.balance = amount;
        this.collateral_name = collateral_name;
        this.collateral_amount = collateral_amount;
        this.customer_id = customer_id;
        this.interest_rate = interest_rate;
        this.status = "Pending";
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
}

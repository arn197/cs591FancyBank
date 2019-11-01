public class Account {
    private int account_number;
    private int customer_id;
    private double balance;
    private String type;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAccount_number() {
        return account_number;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public double getBalance() {
        return balance;
    }

    public String getType() {
        return type;
    }

    public void setAccount_number(int account_number) {
        this.account_number = account_number;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setType(String type) {
        this.type = type;
    }

    Account(int account_number, int customer_id, String type, double balance){
        this.account_number = account_number;
        this.type = type;
        this.balance = balance;
        this.customer_id = customer_id;
        this.active = true;
    }

}

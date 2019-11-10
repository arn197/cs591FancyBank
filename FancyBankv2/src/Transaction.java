public class Transaction {
    private int transaction_id;
    protected String transaction_type;
    private int receiving_account_id;
    private int receiving_user_id;
    private int sending_account_id;
    private int sending_user_id;
    private double amount;

    Transaction(int transaction_id, int receiving_account_id, int receiving_user_id, int sending_account_id, int sending_user_id, double amount){
        this.transaction_id = transaction_id;
        this.transaction_type = "general";
        this.receiving_account_id = receiving_account_id;
        this.sending_account_id = sending_account_id;
        this.sending_user_id = sending_user_id;
        this.receiving_user_id = receiving_user_id;
        this.amount = amount;
    }

    Transaction(int transaction_id, String transaction_type, int receiving_account_id, int receiving_user_id, int sending_account_id, int sending_user_id, double amount) {
        this.transaction_id = transaction_id;
        this.transaction_type = transaction_type;
        this.receiving_account_id = receiving_account_id;
        this.receiving_user_id = receiving_user_id;
        this.sending_account_id = sending_account_id;
        this.sending_user_id = sending_user_id;
        this.amount = amount;
    }

    public int getReceiving_user_id() {
        return receiving_user_id;
    }

    public void setReceiving_user_id(int receiving_user_id) {
        this.receiving_user_id = receiving_user_id;
    }

    public int getSending_user_id() {
        return sending_user_id;
    }

    public void setSending_user_id(int sending_user_id) {
        this.sending_user_id = sending_user_id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getReceiving_account_id() {
        return receiving_account_id;
    }

    public void setReceiving_account_id(int receiving_account_id) {
        this.receiving_account_id = receiving_account_id;
    }

    public int getSending_account_id() {
        return sending_account_id;
    }

    public void setSending_account_id(int sending_account_id) {
        this.sending_account_id = sending_account_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTransaction_type() {
        return transaction_type;
    }
}

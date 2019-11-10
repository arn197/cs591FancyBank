public class StockTransaction extends Transaction{
    private String code;
    private double n_stock;

    public double getN_stock() {
        return n_stock;
    }

    public void setN_stock(int n_stock) {
        this.n_stock = n_stock;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    StockTransaction(int transaction_id, int receiving_account_id, int receiving_user_id, int sending_account_id, int sending_user_id, double amount, String code, int n_stock) {
        super(transaction_id, receiving_account_id, receiving_user_id, sending_account_id, sending_user_id, amount);
        this.transaction_type = "stock";
        this.code = code;
        this.n_stock = n_stock;
    }

    StockTransaction(int transaction_id,
                     String transaction_type,
                     int receiving_account_id,
                     int receiving_user_id,
                     int sending_account_id,
                     int sending_user_id,
                     double amount,
                     String code,
                     double n_stock) {
        super(transaction_id, transaction_type, receiving_account_id, receiving_user_id, sending_account_id, sending_user_id, amount);
        this.code = code;
        this.n_stock = n_stock;
    }
}

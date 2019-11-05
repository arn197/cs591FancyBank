public class StockTransaction extends Transaction{
    private String code;
    private int n_stock;

    public int getN_stock() {
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

    StockTransaction(int receiving_account_id, int receiving_user_id, int sending_account_id, int sending_user_id, double amount, String code, int n_stock) {
        super(receiving_account_id, receiving_user_id, sending_account_id, sending_user_id, amount);
        this.code = code;
        this.n_stock = n_stock;
    }
}

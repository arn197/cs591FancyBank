import java.util.ArrayList;

public class SecurityAccount extends Account {
    private ArrayList<Stock> stocks;

    SecurityAccount(int account_number, int customer_id) {
        super(account_number, customer_id, "Security", -1);
        stocks = new ArrayList<>();
    }

    public void addStock(Stock stock){
        this.stocks.add(stock);
    }

    public void removeStocks(Stock stock){
        this.stocks.remove(stock);
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }
}

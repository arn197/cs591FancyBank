public class Stock {
    private String code;
    private double value;
    private double n_stocks;

    public Stock(String code, double value, double n_stocks){
        this.code = code;
        this.value = value;
        this.n_stocks = n_stocks;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getN_stocks() {
        return n_stocks;
    }

    public void setN_stocks(double n_stocks) {
        this.n_stocks = n_stocks;
    }
}

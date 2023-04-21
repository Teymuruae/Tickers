package tikcs;

public class Tickers2 {
    private String symbol;
    private String changeRate;

    public Tickers2(String symbol, String changeRate) {
        this.symbol = symbol;
        this.changeRate = changeRate;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getChangeRate() {
        return changeRate;
    }
}

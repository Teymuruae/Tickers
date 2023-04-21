import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ApiTest {

    private String url = "https://api.kucoin.com/api/v1/market/allTickers";

    public List<Tickers> callApi() {
        return RestAssured
                .given()
                .when()
                .get(url)
                .then()
                .extract().jsonPath().getList("data.ticker", Tickers.class);

    }


    @Test
    public void test() {
        List<Tickers> tickers = callApi();
        List<Tickers> usdtTickers = tickers.stream().filter(x -> x.getSymbol().endsWith("USDT"))
                .collect(Collectors.toList());
        Assertions.assertTrue(usdtTickers.stream().allMatch(x -> x.getSymbol().endsWith("USDT")));
    }

    @Test
    public void sortedTest(){
        List<Tickers> sortedTickers = callApi().stream().filter(x->x.getSymbol().endsWith("USDT"))
                .sorted(new Comparator<Tickers>() {
            @Override
            public int compare(Tickers o1, Tickers o2) {
                return o2.getChangeRate().compareTo( o1.getChangeRate());   //сортируем по changeRate от большего к меньшему

            }
                }).collect(Collectors.toList());
        List<Tickers> top10 = sortedTickers.stream().limit(10).collect(Collectors.toList());

        Assertions.assertEquals("OM-USDT", top10.get(0).getSymbol());
    }
}

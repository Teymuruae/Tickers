package tikcs;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class TiksTest {

    private String url = "https://api.kucoin.com/api/v1/market/allTickers";

    private List<Pojo> apiCall() {
        return RestAssured
                .given()
                .when()
                .get(url)
                .then().extract().jsonPath().getList("data.ticker", Pojo.class);
    }

    @Test
    public void test1() {
        List<Pojo> tickers = apiCall();

        List<Pojo> usdtTickers = tickers.stream().filter(x -> x.getSymbol().endsWith("USDT")).collect(Collectors.toList());
        Assertions.assertTrue(usdtTickers.stream().allMatch(x -> x.getSymbol().endsWith("USDT")));

    }

    @Test
    public void test2() {
        List<Pojo> tickers = apiCall().stream().filter(x -> x.getSymbol().endsWith("USDT")).collect(Collectors.toList());
        List<Pojo> sortedTickers = tickers.stream().sorted(new Comparator<Pojo>() {
            @Override
            public int compare(Pojo o1, Pojo o2) {
                return o2.getChangeRate().compareTo(o1.getChangeRate());
            }
        }).collect(Collectors.toList());
        List<Pojo> top10 = sortedTickers.stream().limit(10).collect(Collectors.toList());
        System.out.println(top10.get(0).getSymbol());
        Assertions.assertEquals("RNDRDOWN-USDT", top10.get(0).getSymbol());
    }

    @Test
    public void sortLowToHigh() {
        List<Pojo> usdtTickers = apiCall().stream()
                .filter(x -> x.getSymbol().endsWith("USDT")).collect(Collectors.toList());
        List<Pojo> lowCost = usdtTickers.stream().sorted(new Comparator<Pojo>() {
            @Override
            public int compare(Pojo o1, Pojo o2) {
                float result = Float.compare(Float.parseFloat(o1.getChangeRate()), Float.parseFloat(o2.getChangeRate()));
                return (int) result;
            }
        }).collect(Collectors.toList());
        List<Pojo> lowCostTop10 = lowCost.stream().limit(10).collect(Collectors.toList());
        Assertions.assertEquals("RNDRUP-USDT", lowCostTop10.get(0).getSymbol());
    }

    @Test
    public void test3(){
        Map<String, Float> map = new HashMap<>();
        List <String> lowerCaseSymbols = apiCall().stream().map(x->x.getSymbol()
                .toLowerCase()).collect(Collectors.toList());
    }
    @Test
    public void test4(){
        Map<String, Float> map = new HashMap<>();
        apiCall().stream().forEach(x-> map.put(x.getSymbol(), Float.parseFloat( x.getChangeRate())));

    }
    @Test
    public void test5(){
        List<Tickers2> tickers = new ArrayList<>();

                apiCall().stream().forEach(x-> tickers.add(new Tickers2(x.getSymbol(),x.getChangeRate())));

    }



}


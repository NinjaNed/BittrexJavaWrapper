import bittrexJson2JavaObjects.publicApiObjects.*;

import com.google.gson.Gson;

import java.util.List;

public class Example {
    public static void main(String[] args) {

        Bittrex bittrex = new Bittrex(UserCredentials.userApiKey, UserCredentials.userSecret);

        printMarket(bittrex);

        findBiggest24HGainer(bittrex);
    }


    private static void printMarket(Bittrex bittrex) {
        Bittrex.printJson(bittrex.getMarkets());
    }


    private static void findBiggest24HGainer(Bittrex bittrex) {
        Gson gson = new Gson();
        List<MarketSummary> marketSummaries = gson.fromJson(bittrex.getMarketSummaries(), GetMarketSummariesContainer.class).getMarketSummaries();
        String marketName = "";
        Double percentChange = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < marketSummaries.size(); i++) {
            MarketSummary marketSummary = marketSummaries.get(i);
            Double lastVal = marketSummary.getLast();
            Double prevDayVal = marketSummary.getPrevDay();
            Double marketPercentChange = (lastVal-prevDayVal)/prevDayVal;
            if (marketPercentChange > percentChange) {
                marketName = marketSummary.getMarketName();
                percentChange = marketPercentChange;
            }
        }
        System.out.println("Biggest Gainer in the last 24H is " + bittrex.getMarket(marketName).getMarketCurrencyLong()
                + " with " + Double.toString(percentChange*100).substring(0, 6) + "% change.");
    }
}

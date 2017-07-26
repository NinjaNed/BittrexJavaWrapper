import bittrexJson2JavaObjects.publicApiObjects.*;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import java.util.List;
import java.util.Set;

/**
 * junit 4.10 Tests that don't require a valid userApiKey nor userSecret
 */
public class PublicAPITests {

    private final Gson gson = new Gson();
    private final Bittrex bittrex = new Bittrex();

    ///// TESTS OF STATIC FUNCTIONS /////

    //Test SHA-512 algorithm on known values.
    @Test
    public void testHmac() {
        String mockSecret = "key";
        String mockMessage = "data";
        String expectedHmac = "3c5953a18f7303ec653ba170ae334fafa08e3846f2efe317b87efce82376253cb52a8c31ddcde5a3a2eee183c2b34cb91f85e64ddbc325f7692b199473579c58";
        String hmac = Bittrex.getHmacSHA512(mockMessage, mockSecret);
        Assert.assertTrue("HMAC for (Message='data',Secret='key') should be " + expectedHmac, hmac.equalsIgnoreCase(expectedHmac));
    }

    //Test if getJSONFromBittrex() returns a non-empty string given a known query.
    @Test
    public void testGetDataFromBittrex() {
        String url = "https://bittrex.com/api/v1.1/public/getticker?market=BTC-XRP";
        String response = Bittrex.getJSONFromBittrex(url);
        Assert.assertTrue("Data should've been received from Bittrex", !response.isEmpty());
    }

    //Test if getJSONFromBittrex returns an empty string given a bad query.
    @Test
    public void testGetBadDataFromBittrex() {
        System.out.println("*****************EXPECTED EXCEPTION STACK TRACE*****************");
        String url = "https://bittrex.com/api/v1.1/asdfasgaghah";
        String response = Bittrex.getJSONFromBittrex(url);
        Assert.assertTrue("Data should be empty string", response.isEmpty());
        System.out.println("****************************************************************");
    }

    ///// TESTS OF JSON OBJECTS /////
    // Note: If any of these tests fail, it's most likely due to differences in JSON architecture given by bittrex and
    // predefined java object defined in this package to contain them. Make sure these align and test again

    //Test if JSON string from known Bittrex query (getmarkets) can be unpacked into a java object.
    @Test
    public void testGetMarketsQuery() throws NullPointerException {
        String url = "https://bittrex.com/api/v1.1/public/getmarkets";
        String response = Bittrex.getJSONFromBittrex(url);

        GetMarketsContainer getMarketsContainer = gson.fromJson(response, GetMarketsContainer.class);
        Assert.assertNotNull(objNullMsg("GetMarketsContainer"), getMarketsContainer);
        Assert.assertTrue(falseFlagMsg("GetMarketsContainer"), getMarketsContainer.getSuccess());

        List<Market> marketList = getMarketsContainer.getMarkets();
        Assert.assertNotNull(objNullMsg("List of Markets"), marketList);

        Market market = marketList.get(0);
        Assert.assertNotNull(objNullMsg("Market"), market);
        Assert.assertTrue(emptyStringMsg("marketName"), !market.getMarketName().isEmpty());
        // if the marketName isn't empty then assume the rest of the values are also packed correctly
    }

    //Test if JSON string from known Bittrex query (getcurrencies) can be unpacked into a java object.
    @Test
    public void testGetCurrenciesQuery() throws NullPointerException {
        String url = "https://bittrex.com/api/v1.1/public/getcurrencies";
        String response = Bittrex.getJSONFromBittrex(url);

        GetCurrenciesContainer getCurrenciesContainer = gson.fromJson(response, GetCurrenciesContainer.class);
        Assert.assertNotNull(objNullMsg("GetCurrenciesContainer"), getCurrenciesContainer);
        Assert.assertTrue(falseFlagMsg("GetCurrenciesContainer"), getCurrenciesContainer.getSuccess());

        List<Currency> currencyList = getCurrenciesContainer.getCurrencies();
        Assert.assertNotNull(objNullMsg("List of Currencies"), currencyList);

        Currency currency = currencyList.get(0);
        Assert.assertNotNull(objNullMsg("Currency"), currency);
        Assert.assertTrue(emptyStringMsg("coinType"), !currency.getCoinType().isEmpty());
    }

    //Test if JSON string from known Bittrex query (getticker) can be unpacked into a java object.
    @Test
    public void testGetTickerQuery() throws NullPointerException {
        String url = "https://bittrex.com/api/v1.1/public/getticker?market=BTC-STRAT";
        String response = Bittrex.getJSONFromBittrex(url);

        GetTickerContainer getTickerContainer = gson.fromJson(response, GetTickerContainer.class);
        Assert.assertNotNull(objNullMsg("GetTickerContainer"), getTickerContainer);
        Assert.assertTrue(falseFlagMsg("GetTickerContainer"), getTickerContainer.getSuccess());

        Ticker ticker = getTickerContainer.getTicker();
        Assert.assertNotNull(objNullMsg("Ticker"), ticker);

        //It is possible that this fails if Bittrex sends out negative values on queries, but that would be insane.
        Assert.assertTrue(lessThanZeroMsg("ask"), ticker.getAsk()>0);
        Assert.assertTrue(lessThanZeroMsg("bid"), ticker.getBid()>0);
        Assert.assertTrue(lessThanZeroMsg("last"), ticker.getLast()>0);
    }

    //Test if JSON string from known Bittrex query (getmarketsummaries) can be unpacked into a java object.
    @Test
    public void testGetMarketSummariesQuery() throws NullPointerException {
        String url = "https://bittrex.com/api/v1.1/public/getmarketsummaries";
        String response = Bittrex.getJSONFromBittrex(url);

        GetMarketSummariesContainer getMarketSummariesContainer = gson.fromJson(response, GetMarketSummariesContainer.class);
        Assert.assertNotNull(objNullMsg("GetMarketSummariesContainer"), getMarketSummariesContainer);
        Assert.assertTrue(falseFlagMsg("GetMarketSummariesContainer"), getMarketSummariesContainer.getSuccess());

        List<MarketSummary> marketSummariesList = getMarketSummariesContainer.getMarketSummaries();
        Assert.assertNotNull(objNullMsg("List of Market Summaries"), marketSummariesList);

        MarketSummary marketSummary = marketSummariesList.get(0);
        Assert.assertNotNull(objNullMsg("MarketSummary"), marketSummary);
        Assert.assertTrue(emptyStringMsg("marketName"), !marketSummary.getMarketName().isEmpty());
    }

    //Test if JSON string from known Bittrex query (getorderbook with type=both) can be unpacked into a java object.
    @Test
    public void testGetOrderBookQuery() throws NullPointerException {
        String url = "https://bittrex.com/api/v1.1/public/getorderbook?market=BTC-LTC&type=both";
        String response = Bittrex.getJSONFromBittrex(url);

        GetOrderBookContainer getOrderBookContainer = gson.fromJson(response, GetOrderBookContainer.class);
        Assert.assertNotNull(objNullMsg("GetOrderBookContainer"), getOrderBookContainer);
        Assert.assertTrue(falseFlagMsg("GetOrderBookContainer"), getOrderBookContainer.getSuccess());

        OrderBook orderBook = getOrderBookContainer.getOrderBook();
        Assert.assertNotNull(objNullMsg("OrderBook"), orderBook);

        List<OrderBookEntry> buy = orderBook.getBuy();
        Assert.assertNotNull("List of Buy Orders", buy);
        Assert.assertTrue("Size of buy should be > 0", buy.size() > 0);
        Assert.assertTrue("Amount of quantity on a buy order should be > 0", buy.get(0).getQuantity() > 0);

        List<OrderBookEntry> sell = orderBook.getSell();
        Assert.assertNotNull("List of Sell Orders", sell);
        Assert.assertTrue("Size of sell should be > 0", sell.size() > 0);
        Assert.assertTrue("Amount of quantity on a sell order should be > 0", sell.get(0).getQuantity() > 0);
    }

    //Test if JSON string from known Bittrex query (getorderbook with type=buy) can be unpacked into a java object.
    @Test
    public void testGetOrderBookBuyOnlyQuery() {
        String url = "https://bittrex.com/api/v1.1/public/getorderbook?market=BTC-DOGE&type=buy";
        String response = Bittrex.getJSONFromBittrex(url);

        GetOrderBookBuyOnlyContainer getOrderBookBuyOnlyContainer = gson.fromJson(response, GetOrderBookBuyOnlyContainer.class);
        Assert.assertNotNull(objNullMsg("GetOrderBookBuyOnlyContainer"), getOrderBookBuyOnlyContainer);
        Assert.assertTrue(falseFlagMsg("GetOrderBookBuyOnlyContainer"), getOrderBookBuyOnlyContainer.getSuccess());

        List<OrderBookEntry> buy = getOrderBookBuyOnlyContainer.getBuy();
        Assert.assertNotNull("List of Buy Orders", buy);
        Assert.assertTrue("Size of buy should be > 0", buy.size() > 0);
        Assert.assertTrue("Amount of quantity on a buy order should be > 0", buy.get(0).getQuantity() > 0);
    }

    //Test if JSON string from known Bittrex query (getorderbook with type=sell) can be unpacked into a java object.
    @Test
    public void testGetOrderBookSellOnlyQuery() {
        String url = "https://bittrex.com/api/v1.1/public/getorderbook?market=BTC-ETH&type=sell";
        String response = Bittrex.getJSONFromBittrex(url);

        GetOrderBookSellOnlyContainer getOrderBookSellOnlyContainer = gson.fromJson(response, GetOrderBookSellOnlyContainer.class);
        Assert.assertNotNull(objNullMsg("GetOrderBookSellOnlyContainer"), getOrderBookSellOnlyContainer);
        Assert.assertTrue(falseFlagMsg("GetOrderBookSellOnlyContainer"), getOrderBookSellOnlyContainer.getSuccess());

        List<OrderBookEntry> sell = getOrderBookSellOnlyContainer.getSell();
        Assert.assertNotNull("List of Sell Orders", sell);
        Assert.assertTrue("Size of sell should be > 0", sell.size() > 0);
        Assert.assertTrue("Amount of quantity on a sell order should be > 0", sell.get(0).getQuantity() > 0);
    }

    //Test if JSON string from known Bittrex query (getmarkehistory) can be unpacked into a java object.
    @Test
    public void testGetMarketHistoryQuery() {
        String url = "https://bittrex.com/api/v1.1/public/getmarkethistory?market=BTC-SC";
        String response = Bittrex.getJSONFromBittrex(url);

        GetMarketHistoryContainer getMarketHistoryContainer = gson.fromJson(response, GetMarketHistoryContainer.class);
        Assert.assertNotNull(objNullMsg("GetMarketHistoryContainer"), getMarketHistoryContainer);
        Assert.assertTrue(falseFlagMsg("GetMarketHistoryContainer"), getMarketHistoryContainer.getSuccess());

        List<Transaction> transactions = getMarketHistoryContainer.getTransactions();
        Assert.assertNotNull(objNullMsg("List of Transactions"), transactions);

        Transaction transaction = transactions.get(0);
        Assert.assertNotNull(objNullMsg("Transaction"), transaction);
        Assert.assertTrue(emptyStringMsg("TimeStamp"), !transaction.getTimeStamp().isEmpty());
        Assert.assertTrue("ID number should be > 0", transaction.getId() > 0);
    }


    ///// TESTS OF BITTREX WRAPPER /////
    // Note: It's possible that tests will randomly fail if Bittrex updates their database between subsequent requests.
    // Unless the test constantly fails, then it is assumed to be correct.

    @Test
    public void testInitialValues() {
        //initial market states are loaded and nonempty
        List<MarketSummary> initMarketStates = bittrex.getMarketStates();
        Assert.assertNotNull(initMarketStates);
        Assert.assertTrue(initMarketStates.size() > 0);

        //initial markets are loaded and nonempty
        Set<String> marketSet = bittrex.getMarketSet();
        Assert.assertNotNull(marketSet);
        Assert.assertTrue(marketSet.size() > 0);

        Assert.assertTrue(bittrex.getTransactionFee("BTC") > 0);
    }

    @Test
    public void testGetMarkets() {
        String url = "https://bittrex.com/api/v1.1/public/getmarkets";
        String response = Bittrex.getJSONFromBittrex(url);
        String getMarketResponse = bittrex.getMarkets();
        Assert.assertEquals(responseDisagreesMsg, response, getMarketResponse);
    }

    @Test
    public void testGetCurrencies() {
        String url = "https://bittrex.com/api/v1.1/public/getcurrencies";
        String response = Bittrex.getJSONFromBittrex(url);
        String getCurrenciesResponse = bittrex.getCurrencies();
        Assert.assertEquals(responseDisagreesMsg, response, getCurrenciesResponse);
    }

    @Test
    public void testGetTicker() {
        String url = "https://bittrex.com/api/v1.1/public/getticker?market=BTC-XMR";
        String response = Bittrex.getJSONFromBittrex(url);
        String getTickerResponse = bittrex.getTicker("BTC-XMR");
        Assert.assertEquals(responseDisagreesMsg, response, getTickerResponse);
    }

    @Test
    public void testGetMarketSummaries() {
        Bittrex bittrex = new Bittrex();
        String url = "https://bittrex.com/api/v1.1/public/getmarketsummaries";
        String response = Bittrex.getJSONFromBittrex(url);
        String getMarketSummariesResponse = bittrex.getMarketSummaries();
        Assert.assertEquals(responseDisagreesMsg, response, getMarketSummariesResponse);
    }

    @Test
    public void testGetMarketSummary() {
        String url = "https://bittrex.com/api/v1.1/public/getmarketsummary?market=btc-etc";
        String response = Bittrex.getJSONFromBittrex(url);
        String getMarketSumaryResponse = bittrex.getMarketSummary("btc-etc");
        Assert.assertEquals(responseDisagreesMsg, response, getMarketSumaryResponse);
    }

    @Test
    public void testGetOrderBook() {
        String url = "https://bittrex.com/api/v1.1/public/getorderbook?market=BTC-DGB&type=both&depth=50";
        String response = Bittrex.getJSONFromBittrex(url);
        String getOrderBookResponse = bittrex.getOrderBook("BTC-DGB");
        Assert.assertEquals(responseDisagreesMsg, response, getOrderBookResponse);
    }
    @Test
    public void testGetOrderBookType() {
        String url = "https://bittrex.com/api/v1.1/public/getorderbook?market=BTC-ION&type=buy&depth=50";
        String response = Bittrex.getJSONFromBittrex(url);
        String getOrderBookResponse = bittrex.getOrderBook("BTC-ION", "buy");
        Assert.assertEquals(responseDisagreesMsg, response, getOrderBookResponse);
    }

    @Test
    public void testGetOrderBookDepth() {
        String url = "https://bittrex.com/api/v1.1/public/getorderbook?market=BTC-STRAT&type=both&depth=30";
        String response = Bittrex.getJSONFromBittrex(url);
        String getOrderBookResponse = bittrex.getOrderBook("BTC-STRAT", 30);
        Assert.assertEquals(responseDisagreesMsg, response, getOrderBookResponse);
    }

    @Test
    public void testGetOrderBookFull() {
        String url = "https://bittrex.com/api/v1.1/public/getorderbook?market=BTC-OMG&type=sell&depth=22";
        String response = Bittrex.getJSONFromBittrex(url);
        String getOrderBookResponse = bittrex.getOrderBook("BTC-OMG", "sell", 22);
        Assert.assertEquals(responseDisagreesMsg, response, getOrderBookResponse);
    }

    @Test
    public void testGetMarketHistory() {
        String url = "https://bittrex.com/api/v1.1/public/getmarkethistory?market=BTC-DASH";
        String response = Bittrex.getJSONFromBittrex(url);
        String getMarketHistoryResponse = bittrex.getMarketHistory("BTC-DASH");
        Assert.assertEquals(responseDisagreesMsg, response, getMarketHistoryResponse);
    }

    private String objNullMsg(String objectName) {
        return objectName + " should not be null. Please review discrepencies in JSON architecture between Bittrex and this package.";
    }

    private String falseFlagMsg(String objectName) {
        return "Success flag for " + objectName + " should be true.";
    }

    private String emptyStringMsg(String objectName) {
        return objectName + " should not be the empty string.";
    }

    private String lessThanZeroMsg(String variableName) {
        return "Double variable : " + variableName + " should be > 0";
    }

    private final String responseDisagreesMsg = "Direct response and method response should agree";
}
import bittrexJson2JavaObjects.accountApiObjects.*;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class AccountAPITests {

    private final String userApiKey = UserCredentials.userApiKey;
    private final String userSecret = UserCredentials.userSecret;

    final Gson gson = new Gson();
    private final Bittrex bittrex = new Bittrex(userApiKey, userSecret);

    @Test
    public void testGetBalances() throws NullPointerException {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
        String response = bittrex.getBalances();
        Assert.assertNotNull(nullResponseMsg, response);
        GetBalancesContainer getBalancesContainer = gson.fromJson(response, GetBalancesContainer.class);
        Assert.assertNotNull(objNullMsg("GetBalancesContainer"), getBalancesContainer);
        Assert.assertTrue(falseFlagMsg, getBalancesContainer.getSuccess());
        Balance balance = getBalancesContainer.getBalances().get(0);
        Assert.assertNotNull(objNullMsg("Balance"), balance);
        Assert.assertNotNull("Currency name should not be null", balance.getCurrency());
    }

    @Test
    public void testGetBalance() throws NullPointerException {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
        String response = bittrex.getBalance("LTC");
        Assert.assertNotNull(nullResponseMsg, response);
        GetBalanceContainer getBalanceContainer = gson.fromJson(response, GetBalanceContainer.class);
        Assert.assertNotNull(objNullMsg("GetBalancesContainer"), getBalanceContainer);
        Assert.assertTrue(falseFlagMsg, getBalanceContainer.getSuccess());
        Balance balance = getBalanceContainer.getBalance();
        Assert.assertNotNull(objNullMsg("Balance"), balance);
        Assert.assertNotNull("Currency name should not be null", balance.getCurrency());
    }

    @Test
    public void testBadCoinGetBalance() {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
        String badcoin = "SHTCOIN";
        String response = bittrex.getBalance(badcoin);
        Assert.assertNull("Response should be null since coin does not exist", response);
    }

    @Test
    public void testGetDepositAddress() {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
        String response = bittrex.getBalance("DOGE");
        Assert.assertNotNull(nullResponseMsg, response);
        GetDepositAddressContainer getDepositAddressContainer = gson.fromJson(response, GetDepositAddressContainer.class);
        Assert.assertNotNull(objNullMsg("GetDepositAddressContainer"), getDepositAddressContainer);
        if (getDepositAddressContainer.getSuccess()) {
            DepositAddress depositAddress = getDepositAddressContainer.getDepositAddress();
            Assert.assertNotNull(objNullMsg("Deposit Address"), depositAddress);
            Assert.assertNotNull(depositAddress.getCurrency());
            Assert.assertNotNull(depositAddress.getCryptoAddress());
        } else {
            Assert.assertEquals(falseFlagConsistentMsg, "ADDRESS_GENERATING", getDepositAddressContainer.getMessage());
        }
    }

    @Test
    public void testWithdraw() {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
    }

    @Test
    public void testGetOrder() {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
        String fakeOrder = "fakeOrder";
        String response = bittrex.getOrder(fakeOrder);
        Assert.assertNotNull(nullResponseMsg, response);
        GetOrderContainer getOrderContainer = gson.fromJson(response, GetOrderContainer.class);
        Assert.assertNotNull(objNullMsg("GetOrderContainer"), getOrderContainer);
        Assert.assertFalse(getOrderContainer.getSuccess());
        Assert.assertEquals(falseFlagConsistentMsg, "UUID_INVALID", getOrderContainer.getMessage());
    }

//     If you want to test the validity of of the function, fill in the uuid with a valid uuid from a past account order. Then uncomment
//    @Test
//    public void testGetOrderValidUUID() {
//        Assert.assertTrue(badCrendentialMsg, checkCredentials());
//        String uuid = ""; //fill in
//        String response = bittrex.getOrder(uuid);
//        Assert.assertNotNull(nullResponseMsg, response);
//        GetOrderContainer getOrderContainer = gson.fromJson(response, GetOrderContainer.class);
//        Assert.assertNotNull(objNullMsg("GetOrderContainer"), getOrderContainer);
//        Order order = getOrderContainer.getOrder();
//        Assert.assertNotNull(objNullMsg("Order"), order);
//        Assert.assertNotNull("Exchange", order.getExchange());
//        Assert.assertEquals("Uuids should match", uuid, order.getOrderUuid());
//    }

    @Test
    public void testGetOrderHistory() {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
        String response = bittrex.getOrderHistory();
        Assert.assertNotNull(nullResponseMsg, response);
        GetOrderHistoryContainer getOrderHistoryContainer = gson.fromJson(response, GetOrderHistoryContainer.class);
        Assert.assertNotNull(objNullMsg("GetOrderHistoryContainer"), getOrderHistoryContainer);
        Assert.assertTrue(falseFlagMsg, getOrderHistoryContainer.getSuccess());
        if (getOrderHistoryContainer.getPastOrders() != null) {
            PastOrder pastOrder = getOrderHistoryContainer.getPastOrders().get(0);
            Assert.assertNotNull(objNullMsg("PastOrder"), pastOrder);
            Assert.assertNotNull(objNullMsg("Exchange"), pastOrder.getExchange());
            Assert.assertNotNull(objNullMsg("Condition"), pastOrder.getCondition());
        }
    }

    @Test
    public void testGetWithdrawlHistory() {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
        String response = bittrex.getWithdrawalHistory();
        Bittrex.printJson(response);
        Assert.assertNotNull(nullResponseMsg, response);
        GetWithdrawlHistoryContainer getWithdrawlHistoryContainer = gson.fromJson(response, GetWithdrawlHistoryContainer.class);
        Assert.assertNotNull(objNullMsg("GetWithdrawlHistoryContainer"), getWithdrawlHistoryContainer);
        Assert.assertTrue(falseFlagMsg, getWithdrawlHistoryContainer.getSuccess());

        if (getWithdrawlHistoryContainer.getWithdrawls() != null && getWithdrawlHistoryContainer.getWithdrawls().size() > 0) {
            AccountTransaction pastWithdrawlTransaction = getWithdrawlHistoryContainer.getWithdrawls().get(0);
            Assert.assertNotNull(objNullMsg("Account Transaction (Withdrawl)"), pastWithdrawlTransaction);
            Assert.assertNotNull(objNullMsg("Currency"), pastWithdrawlTransaction.getCurrency());
        }
    }

    @Test
    public void testGetWithdrawlHistoryBTC() {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
        String response = bittrex.getWithdrawalHistory("BTC");
        Assert.assertNotNull(nullResponseMsg, response);
        GetWithdrawlHistoryContainer getWithdrawlHistoryContainer = gson.fromJson(response, GetWithdrawlHistoryContainer.class);
        Assert.assertNotNull(objNullMsg("GetWithdrawlHistoryContainer"), getWithdrawlHistoryContainer);
        Assert.assertTrue(falseFlagMsg, getWithdrawlHistoryContainer.getSuccess());
    }

    @Test
    public void testGetDepositHistory() {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
        String response = bittrex.getDepositHistory();
        Assert.assertNotNull(nullResponseMsg, response);
        GetDepositHistoryContainer getDepositHistoryContainer = gson.fromJson(response, GetDepositHistoryContainer.class);
        Assert.assertNotNull(objNullMsg("GetDepositHistoryContainer"), getDepositHistoryContainer);
        Assert.assertTrue(falseFlagMsg, getDepositHistoryContainer.getSuccess());
        if (getDepositHistoryContainer.getDeposits() != null && getDepositHistoryContainer.getDeposits().size() > 0) {
            AccountTransaction pastDepositTransaction = getDepositHistoryContainer.getDeposits().get(0);
            Assert.assertNotNull(objNullMsg("Account Transaction (Withdrawl)"), pastDepositTransaction);
            Assert.assertNotNull(objNullMsg("Currency"), pastDepositTransaction.getCurrency());
        }
    }

    @Test
    public void testGetDepositHistoryBTC() {
        Assert.assertTrue(badCrendentialMsg, checkCredentials());
        String response = bittrex.getDepositHistory("BTC");
        Assert.assertNotNull(nullResponseMsg, response);
        GetDepositHistoryContainer getDepositHistoryContainer = gson.fromJson(response, GetDepositHistoryContainer.class);
        Assert.assertNotNull(objNullMsg("GetDepositHistoryContainer"), getDepositHistoryContainer);
        Assert.assertTrue(falseFlagMsg, getDepositHistoryContainer.getSuccess());
    }

    private boolean checkCredentials() {
        if (userSecret.isEmpty() || userApiKey.isEmpty()) {
            return false;
        } else {
            //As long as the apikey and secret are valid, then JSON from getbalance should return true even if there is nothing in the wallet.
            GetBalanceContainer gbc = gson.fromJson(bittrex.getBalance("BTC"), GetBalanceContainer.class);
            return gbc.getSuccess();
        }

    }

    private final String badCrendentialMsg = "Invalid Apikey and/or Secret. Valid credentials required for account queries.";

    private String objNullMsg(String objectName) {
        return objectName + " should not be null. Please review discrepencies in JSON architecture between Bittrex and this package.";
    }

    private final String falseFlagMsg = "Success flag should be true";

    private final String falseFlagConsistentMsg = "False flag should result in same message";

    private final String nullResponseMsg = "Response from Bittrex should not be null";
}


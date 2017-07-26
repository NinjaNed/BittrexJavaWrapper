/**
 * Created by Ned Udomkesmalee on 7/18/17.
 */
package bittrexJson2JavaObjects.publicApiObjects;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderBook {

    @SerializedName("buy")
    @Expose
    private List<OrderBookEntry> buy = null;
    @SerializedName("sell")
    @Expose
    private List<OrderBookEntry> sell = null;

    public List<OrderBookEntry> getBuy() {
        return buy;
    }

    public void setBuy(List<OrderBookEntry> buy) {
        this.buy = buy;
    }

    public List<OrderBookEntry> getSell() {
        return sell;
    }

    public void setSell(List<OrderBookEntry> sell) {
        this.sell = sell;
    }

}

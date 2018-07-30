package app.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
    This class exists only to make serialization onto a message queue easy
*/
@XmlRootElement(name = "trade")
@JacksonXmlRootElement(localName = "trade")
public class Order {

    @XmlElement(name = "id")
    @JsonProperty("id")
    private int strategyId;

    @XmlElement(name = "buy")
    @JsonProperty("buy")
    private boolean buyTrade;

    @XmlElement(name = "price")
    @JsonProperty("price")
    private double price;

    @XmlElement(name = "size")
    @JsonProperty("size")
    private int tradeSize;

    @XmlElement(name = "whenAsDate")
    @JsonProperty("whenAsDate")
    private String timeTransacted;

    @XmlElement(name = "stock")
    @JsonProperty("stock")
    private String stock;

    public int getId() {
        return strategyId;
    }

    public void setId(int id) {
        this.strategyId = id;
    }

    public boolean isBuyTrade() {
        return buyTrade;
    }

    public void setBuyTrade(boolean buyTrade) {
        this.buyTrade = buyTrade;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTradeSize() {
        return tradeSize;
    }

    public void setTradeSize(int tradeSize) {
        this.tradeSize = tradeSize;
    }

    public String getTimeTransacted() {
        return timeTransacted;
    }

    public void setTimeTransacted(String timeTransacted) {
        this.timeTransacted = timeTransacted;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    private class TradeDTO extends Order {

    }
}

package app.entities;

import app.entities.strategies.Strategy;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@Entity
@Table(name = "Trades")
@XmlRootElement(name = "trade")
@JacksonXmlRootElement(localName = "trade")
public class Trade {

    public enum TransactionState { FILLED, PARTIALLY_FILLED, REJECTED };

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "Buy")
    private Boolean buyTrade;

    @Column(name = "Price")
    private Double price;

    @Column(name = "TradeSize")
    private Integer tradeSize;

    @Column(name = "TimeTransacted")
    private LocalDateTime timeTransacted;

    @Column(name = "TransactionState")
    @Enumerated(EnumType.STRING)
    private TransactionState transactionState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StrategyId")
    private Strategy strategy;

    @OneToOne
    @JoinColumn(name = "Stock")
    private Stock stock;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Trade)) {
            return false;
        }
        Trade t = (Trade) o;
        return this.getId() == t.getId();
    }

    @Override
    public int hashCode() {
        return 31 * this.getId();
    }

    public Integer getId() {
        return id;
    }

    public Boolean isBuyTrade() {
        return buyTrade;
    }

    public void setBuyTrade(Boolean buyTrade) {
        this.buyTrade = buyTrade;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getTradeSize() {
        return tradeSize;
    }

    public void setTradeSize(Integer tradeSize) {
        this.tradeSize = tradeSize;
    }

    public LocalDateTime getTimeTransacted() {
        return timeTransacted;
    }

    public void setTimeTransacted(LocalDateTime timeTransacted) {
        this.timeTransacted = timeTransacted;
    }

    public TransactionState getTransactionState() {
        return transactionState;
    }

    public void setTransactionState(TransactionState transactionState) {
        this.transactionState = transactionState;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ticker")
    @JsonIdentityReference(alwaysAsId = true)
    public Stock getStock() {
        return stock;
    }

    // Used to easily receive JMS messages
    public class TradeDTO extends Order {

        public TradeDTO() {}

        @XmlElement(name = "result")
        private String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

    }
}

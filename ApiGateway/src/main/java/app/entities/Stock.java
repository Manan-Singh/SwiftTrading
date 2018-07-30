package app.entities;

import javax.persistence.*;

@Entity
@Table(name = "Stocks")
public class Stock {


    @Id
    @Column(name = "Ticker", unique = true)
    private String ticker;

    @Column(name = "Price")
    private Double price;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Stock)) {
            return false;
        }
        Stock s = (Stock) o;
        return this.getTicker().equals(s.getTicker());
    }

    @Override
    public int hashCode() {
        return 31 * this.getTicker().hashCode();
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

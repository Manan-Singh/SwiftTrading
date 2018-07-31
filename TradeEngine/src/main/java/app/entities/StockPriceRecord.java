package app.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="StockPriceRecords")
public class StockPriceRecord implements Serializable {

    public StockPriceRecord() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Ticker", unique = true)
    private String ticker;

    @Column(name = "TimeInspected")
    private Timestamp timeInspected;

    @Column(name = "Price")
    private Double price;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof StockPriceRecord)) {
            return false;
        }
        StockPriceRecord s = (StockPriceRecord) o;
        return this.id == s.id;
    }

    @Override
    public int hashCode() {
        return 31 * this.id;
    }

    public Integer getId() {
        return id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Timestamp getTimeInspected() {
        return timeInspected;
    }

    public void setTimeInspected(Timestamp timeInspected) {
        this.timeInspected = timeInspected;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

package app.entities.strategies;

import app.entities.Trade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Strategies")
@Inheritance(strategy = InheritanceType.JOINED)
// the following annotation fixes /strategies/{id} path when trying to display json
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Strategy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "IsActive")
    @JsonProperty("isActive")
    private Boolean isActive = true;

    @Column(name = "Name")
    @JsonProperty("name")
    private String name;

    @Column(name = "Close")
    @JsonProperty("close")
    private Integer closePercentage;

    @Column(name = "EntrySize")
    @JsonProperty("entrySize")
    private Integer entrySize;

    @Column(name = "Ticker")
    @JsonProperty("ticker")
    private String ticker;

    @Column(name = "ProfitValue")
    private Double profitValue;

    @OneToMany(mappedBy = "strategy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Trade> trades;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Strategy)) {
            return false;
        }
        Strategy s = (Strategy) o;
        return this.getId() == s.getId();
    }

    @Override
    public int hashCode() {
        return 31 * this.getId();
    }

    public Integer getId() {
        return id;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Integer getClosePercentage() {
        return closePercentage;
    }

    public void setClosePercentage(Integer closePercentage) {
        this.closePercentage = closePercentage;
    }

    public Integer getEntrySize() {
        return entrySize;
    }

    public void setEntrySize(Integer entrySize) {
        this.entrySize = entrySize;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Double getProfitValue() { return profitValue; }

    public void setProfitValue(Double profitValue) { this.profitValue = profitValue; }

    public void setTrades(List<Trade> trades) { this.trades = trades; }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public List<Trade> getTrades() {
        return trades;
    }
}

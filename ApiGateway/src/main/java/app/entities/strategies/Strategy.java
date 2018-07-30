package app.entities.strategies;

import app.entities.Stock;
import app.entities.Trade;
import com.fasterxml.jackson.annotation.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "Entry")
    private Integer entryPosition;

    @Column(name = "StrategyType")
    private String strategyType;

    @Column(name = "Close")
    private Integer closePosition;

    @Column(name = "Name")
    private String name;

    @OneToMany(mappedBy = "strategy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Trade> trades;

    @OneToOne
    @JoinColumn(name = "Stock")
    private Stock stock;

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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getEntryPosition() {
        return entryPosition;
    }

    public void setEntryPosition(Integer entryPosition) {
        this.entryPosition = entryPosition;
    }

    public Integer getClosePosition() {
        return closePosition;
    }

    public void setClosePosition(Integer closePosition) {
        this.closePosition = closePosition;
    }

    public String getStrategyType() { return strategyType; }

    public void setStrategyType(String strategyType) { this.strategyType = strategyType; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ticker")
    @JsonIdentityReference(alwaysAsId = true)
    public Stock getStock() {
        return stock;
    }

    public List<Trade> getTrades() {
        return trades;
    }
}

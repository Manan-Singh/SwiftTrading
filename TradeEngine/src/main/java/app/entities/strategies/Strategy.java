package app.entities.strategies;

import app.entities.Stock;
import app.entities.Trade;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Strategies")
@Inheritance(strategy = InheritanceType.JOINED)
public class Strategy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "Entry")
    private Integer entryPosition;

    @Column(name = "Close")
    private Integer closePosition;

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

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ticker")
    @JsonIdentityReference(alwaysAsId = true)
    public Stock getStock() {
        return stock;
    }

    public List<Trade> getTrades() {
        return trades;
    }
}

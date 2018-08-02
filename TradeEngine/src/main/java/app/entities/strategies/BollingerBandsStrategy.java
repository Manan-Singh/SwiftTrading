package app.entities.strategies;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "BollingerBandsStrategies")
@PrimaryKeyJoinColumn(name = "Id")
public class BollingerBandsStrategy extends Strategy {

    public BollingerBandsStrategy() {
    }

    @Column(name = "Period")
    private Integer period;

    @Column(name = "StdDev")
    private Double stdDev;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Double getStdDev() {
        return stdDev;
    }

    public void setStdDev(Double stdDev) {
        this.stdDev = stdDev;
    }
}

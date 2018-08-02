package app.entities.strategies;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "TwoMovingAveragesStrategies")
@PrimaryKeyJoinColumn(name = "Id")
public class TwoMovingAveragesStrategy extends Strategy {

    public TwoMovingAveragesStrategy() {
    }

    @Column(name = "LongTime")
    private Integer longTime;

    @Column(name = "ShortTime")
    private Integer shortTime;

    public Integer getLongTime() {
        return longTime;
    }

    public void setLongTime(Integer longTime) {
        this.longTime = longTime;
    }

    public Integer getShortTime() {
        return shortTime;
    }

    public void setShortTime(Integer shortTime) {
        this.shortTime = shortTime;
    }
}

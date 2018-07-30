package app.entities.strategies;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "TwoMovingAveragesStrategies")
@PrimaryKeyJoinColumn(name = "Id")
public class TwoMovingAveragesStrategy extends Strategy {

    @Column(name = "LongTime")
    private Integer longTimeMillis;

    @Column(name = "ShortTime")
    private Integer shortTimeMills;

    @Column(name = "LastLongAvg")
    private Double lastLongAvg;

    @Column(name = "LastShortAvg")
    private Double lastShortAvg;
}

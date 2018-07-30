package app.entities.strategies;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ChaosStrategies")
@PrimaryKeyJoinColumn(name = "Id")
public class ChaosStrategy extends Strategy {
}

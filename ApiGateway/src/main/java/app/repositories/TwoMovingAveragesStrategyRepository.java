package app.repositories;

import app.entities.strategies.TwoMovingAveragesStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwoMovingAveragesStrategyRepository extends JpaRepository<TwoMovingAveragesStrategy, Integer> {

}

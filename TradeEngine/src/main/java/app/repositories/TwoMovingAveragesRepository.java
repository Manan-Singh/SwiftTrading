package app.repositories;

import app.entities.strategies.TwoMovingAveragesStrategy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoMovingAveragesRepository extends JpaRepository<TwoMovingAveragesStrategy, Integer> {
}

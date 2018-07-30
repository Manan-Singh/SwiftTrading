package app.repositories;

import app.entities.strategies.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Integer> {

    List<Strategy> findByIsActive(boolean isActive);

    List<Strategy> findAllByStrategyType(String strategyType);
}

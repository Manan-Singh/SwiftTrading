package app.repositories;

import app.entities.strategies.BollingerBandsStrategy;
import app.entities.strategies.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BollingerBandsStrategyRepository extends JpaRepository<BollingerBandsStrategy, Integer> {

}

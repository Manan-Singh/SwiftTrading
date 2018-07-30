package app.repositories;

import app.entities.strategies.BollingerBandsStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BollingerBandsStrategyRepository extends JpaRepository<BollingerBandsStrategy, Integer> {

}

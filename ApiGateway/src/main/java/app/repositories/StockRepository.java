package app.repositories;

import app.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    @Query("SELECT s FROM Stock s WHERE s.ticker IN ( SELECT t.stock.ticker FROM Strategy t WHERE t.isActive = true)")
    List<Stock> findAllActiveStocks();
}

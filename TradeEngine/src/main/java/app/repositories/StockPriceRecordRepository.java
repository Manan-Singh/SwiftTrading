package app.repositories;

import app.entities.StockPriceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPriceRecordRepository extends JpaRepository<StockPriceRecord, Integer> {

    @Query(value = "SELECT DISTINCT Ticker FROM Strategies WHERE isActive = true", nativeQuery = true)
    List<String> getAllActiveStocks();

    @Query(value = "SELECT AVG(Price) FROM StockPriceRecords WHERE Ticker = ?2 AND TimeInspected >= NOW() - INTERVAL ?1 SECOND", nativeQuery = true)
    Double getAverageStockPrice(int period, String ticker);

    @Query(value = "SELECT Price FROM StockPriceRecords WHERE Ticker = ?1 order by TimeInspected desc LIMIT 1", nativeQuery = true)
    Double getMostRecentStockPrice(String ticker);

}

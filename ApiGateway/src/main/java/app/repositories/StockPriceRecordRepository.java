package app.repositories;

import app.entities.StockPriceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockPriceRecordRepository extends JpaRepository<StockPriceRecord, Integer> {
}

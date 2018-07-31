package app.repositories;

import app.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface TradeRepository extends JpaRepository<Trade, Integer> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Trades (Buy, StrategyId, Price, TradeSize, TimeTransacted, TransactionState) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void saveNewTrade(boolean buy, int strategyId, double price, int tradeSize, String time, String state);
}

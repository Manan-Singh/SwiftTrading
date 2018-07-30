package app.repositories;

import app.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface TradeRepository extends JpaRepository<Trade, Integer> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Trades (Buy, StrategyId, Price, TradeSize, Stock, TimeTransacted, TransactionState) " +
            "VALUES (?1, ?2, ?3, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
    public void saveNewTrade(boolean buy, int strategyId, double price, int tradeSize, String ticker, String time, String state);
}

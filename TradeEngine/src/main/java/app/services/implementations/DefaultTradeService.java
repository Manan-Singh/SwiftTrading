package app.services.implementations;

import app.repositories.TradeRepository;
import app.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTradeService implements TradeService  {

    @Autowired
    private TradeRepository repo;

    @Override
    public void saveTrade(boolean buy, int strategyId, double price, int tradeSize, String ticker, String time, String state) {
        repo.saveNewTrade(buy, strategyId, price, tradeSize, ticker, time, state);
    }
}

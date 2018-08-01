package app.services.implementations;

import app.entities.Trade;
import app.repositories.TradeRepository;
import app.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultTradeService implements TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Override
    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }
}

package app.services.implementations;

import app.entities.Stock;
import app.repositories.StockRepository;
import app.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultStockService implements StockService {

    @Autowired
    private StockRepository repo;

    @Override
    public List<Stock> getAllStocks() {
        return repo.findAll();
    }

    @Override
    public List<Stock> getAllActiveStocks() {
        return repo.findAllActiveStocks();
    }

    @Override
    public Stock getStock(String ticker) {
        return repo.findStockByTicker(ticker);
    }

    @Override
    @Async
    public void save(Stock s) {
        repo.save(s);
    }

}

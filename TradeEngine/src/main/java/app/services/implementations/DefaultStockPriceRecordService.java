package app.services.implementations;

import app.entities.StockPriceRecord;
import app.repositories.StockPriceRecordRepository;
import app.services.StockPriceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultStockPriceRecordService implements StockPriceRecordService {

    @Autowired
    private StockPriceRecordRepository repo;

    @Override
    public List<String> getAllActiveStocks() {
        return repo.getAllActiveStocks();
    }

    @Override
    public void save(StockPriceRecord spr) {
        repo.save(spr);
    }

    @Override
    public Double getAverageStockPrice(int period, String ticker) {
        return repo.getAverageStockPrice(period, ticker);
    }

    @Override
    public Double getStdDev(int period, String ticker) {
        return repo.getStdDevStockPrice(period, ticker);
    }

    @Override
    public Double getMostRecentStockPrice(String ticker) {
        return repo.getMostRecentStockPrice(ticker);
    }

}

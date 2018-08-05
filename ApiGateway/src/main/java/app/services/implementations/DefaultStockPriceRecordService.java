package app.services.implementations;

import app.repositories.StockPriceRecordRepository;
import app.services.StockPriceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultStockPriceRecordService implements StockPriceRecordService {

    @Autowired
    private StockPriceRecordRepository repo;

    @Override
    public void refresh() {
        repo.deleteAll();
    }
}

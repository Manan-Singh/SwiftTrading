package app.services;

import app.entities.StockPriceRecord;

import java.util.List;

public interface StockPriceRecordService {

    List<String> getAllActiveStocks();

    void save(StockPriceRecord spr);

    Double getAverageStockPrice(int period, String ticker);

    Double getMostRecentStockPrice(String ticker);
}

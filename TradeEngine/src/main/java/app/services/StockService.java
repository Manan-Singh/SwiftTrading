package app.services;

import app.entities.Stock;

import java.util.List;

public interface StockService {

    List<Stock> getAllStocks();

    List<Stock> getAllActiveStocks();

    Stock getStock(String ticker);

    void save(Stock s);
}

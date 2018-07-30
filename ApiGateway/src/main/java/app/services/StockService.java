package app.services;

import app.entities.Stock;

import java.util.List;

public interface StockService {

    List<Stock> getAllStocks();

    List<Stock> getAllActiveStocks();
}

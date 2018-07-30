package app.controllers;

import app.entities.Stock;
import app.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private StockService service;

    @RequestMapping("/stocks")
    public List<Stock> getAllStocks() {
        return service.getAllStocks();
    }

    @RequestMapping("/stocks/active")
    public List<Stock> getAllActiveStocks() {
        return service.getAllActiveStocks();
    }
}

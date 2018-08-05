package app.controllers;

import app.services.StockPriceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class StockPriceRecordController {

    @Autowired
    private StockPriceRecordService stockPriceRecordService;

    @GetMapping("/stocks/refresh")
    public void refresh() {
        stockPriceRecordService.refresh();
    }
}

package app.controllers;

import app.entities.Trade;
import app.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @GetMapping("/trades")
    public List<Trade> getAllTrades(){
        return tradeService.getAllTrades();
    }
}

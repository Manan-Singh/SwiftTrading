package app.controllers;

import app.entities.Trade;
import app.entities.strategies.BollingerBandsStrategy;
import app.entities.strategies.Strategy;
import app.services.StrategyService;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @GetMapping("/strategies/active")
    public List<Strategy> getAllActiveStrategies() {
        return strategyService.getActiveStrategies();
    }

    @GetMapping("/strategies")
    public List<Strategy> getAllStrategies() {
        return strategyService.getAllStrategies();
    }

    @GetMapping("/strategies/{id}")
    public Strategy getStrategyById(@PathVariable("id") Integer id) { return strategyService.getStrategyById(id); }

    @PostMapping("/strategies")
    public Strategy addStrategy(@RequestBody Strategy strategy) {return strategyService.createStrategy(strategy); }

    @GetMapping("/strategies/bollinger")
    public List<BollingerBandsStrategy> getAllBollingerBandsStrategy(){
        return strategyService.getAllBollingerStrategies();
    }

    @PostMapping("/strategies/bollinger")
    public BollingerBandsStrategy addBollingerBandsStrategy(@RequestBody BollingerBandsStrategy strategy){
        return strategyService.createBollingerStrategy(strategy);
    }

    @PutMapping("/strategies")
    public Strategy updateStrategy(@RequestBody Strategy strategy) {return strategyService.updateStrategy(strategy); }

    @DeleteMapping("/strategies/{id}")
    public void deleteStrategy(@PathVariable("id") Integer id){
        // TODO: only delete inactive strategies
        strategyService.deleteStrategyById(id);
    }

    @GetMapping("/strategies/{id}/trades")
    public List<Trade> getTradesByStrategyId(@PathVariable("id") Integer id){
        return strategyService.getTradesByStrategyId(id);
    }

    //@GetMapping("/trades")
    //public List<Trades> getAllTrades
}

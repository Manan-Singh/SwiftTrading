package app.controllers;

import app.entities.Trade;
import app.entities.strategies.Strategy;
import app.services.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @GetMapping("/strategies")
    public List<Strategy> getAllStrategies() {
        return strategyService.getAllStrategies();
    }

    @GetMapping("/strategies/{id}")
    public Strategy getStrategyById(@PathVariable("id") Integer id) { return strategyService.getStrategyById(id); }

    @GetMapping("/strategies/active")
    public List<Strategy> getAllActiveStrategies() {
        return strategyService.getActiveStrategies();
    }

    @PostMapping("/strategies")
    public Strategy addStrategy(@RequestBody Strategy strategy) {return strategyService.createStrategy(strategy); }

    @PutMapping("/strategies")
    public Strategy updateStrategy(@RequestBody Strategy strategy) {return strategyService.updateStrategy(strategy); }

    @DeleteMapping("/strategies/{id}")
    public void deleteStrategy(@PathVariable("id") Integer id){ strategyService.deleteStrategyById(id);}

    @GetMapping("/strategies/{id}/trades")
    public List<Trade> getTradesByStrategyId(@PathVariable("id") Integer id){
        return strategyService.getTradesByStrategyId(id);
    }

    //@GetMapping("/trades")
    //public List<Trades> getAllTrades

    @GetMapping("/strategy/{strategyType}")
    public List<Strategy> getStrategiesByStrategyType(@PathVariable("stategyType") String strategyType){
        return strategyService.getStrategiesByStrategyType(strategyType);
    }
}

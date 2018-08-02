package app.controllers;

import app.entities.Trade;
import app.entities.strategies.BollingerBandsStrategy;
import app.entities.strategies.Strategy;
import app.entities.strategies.TwoMovingAveragesStrategy;
import app.services.StrategyService;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(maxAge = 3600)
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
    public List<BollingerBandsStrategy> getAllBollingerBandsStrategies(){
        return strategyService.getAllBollingerStrategies();
    }

    @PostMapping("/strategies/bollinger")
    public BollingerBandsStrategy addBollingerBandsStrategy(@RequestBody BollingerBandsStrategy strategy){
        return strategyService.createOrSaveBollingerStrategy(strategy);
    }

    @GetMapping("/strategies/movingaverages")
    public List<TwoMovingAveragesStrategy> getAllMovingAveragesStrategies(){
        return strategyService.getAllMovingAveragesStrategies();
    }

    @PostMapping("/strategies/movingaverages")
    public TwoMovingAveragesStrategy addMovingAveragesStrategy(@RequestBody TwoMovingAveragesStrategy strategy){
        return strategyService.createOrSaveMovingAveragesStrategy(strategy);
    }

    @PutMapping("/strategies/{id}")
    public Strategy updateStrategy(@PathVariable("id") Integer id, @RequestBody Strategy strategy) {
        return strategyService.updateStrategy(id, strategy);
    }

    @PutMapping("/strategies/{id}/toggleIsActive")
    public Strategy toggleIsActiveField(@PathVariable("id") Integer id) {return strategyService.toggleIsActiveField(id);}

    @DeleteMapping("/strategies/{id}")
    public ResponseEntity<?> deleteStrategy(@PathVariable("id") Integer id){
        Strategy strategy = strategyService.getStrategyById(id);
        if (!strategy.getIsActive()){
            strategyService.deleteStrategyById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // Status Code 409
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/strategies/{id}/trades")
    public List<Trade> getTradesByStrategyId(@PathVariable("id") Integer id){
        return strategyService.getTradesByStrategyId(id);
    }

    //@GetMapping("/trades")
    //public List<Trades> getAllTrades
}

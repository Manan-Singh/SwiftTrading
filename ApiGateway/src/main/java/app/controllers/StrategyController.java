package app.controllers;

import app.entities.strategies.Strategy;
import app.services.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @RequestMapping("/strategies")
    public List<Strategy> getAllStrategies() {
        return strategyService.getAllStrategies();
    }

    @RequestMapping("/strategies/active")
    public List<Strategy> getAllActiveStrategies() {
        return strategyService.getActiveStrategies();
    }
}

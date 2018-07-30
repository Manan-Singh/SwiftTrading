package app.services.implementations;

import app.entities.strategies.Strategy;
import app.repositories.StrategyRepository;
import app.services.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultStrategyService implements StrategyService {

    @Autowired
    private StrategyRepository strategyRepository;

    @Override
    public List<Strategy> getAllStrategies() {
        return strategyRepository.findAll();
    }

    @Override
    public List<Strategy> getActiveStrategies() {
        return strategyRepository.findByIsActive(true);
    }
}

package app.services.implementations;

import app.entities.Trade;
import app.entities.strategies.BollingerBandsStrategy;
import app.entities.strategies.Strategy;
import app.entities.strategies.TwoMovingAveragesStrategy;
import app.repositories.BollingerBandsStrategyRepository;
import app.repositories.StrategyRepository;
import app.repositories.TwoMovingAveragesStrategyRepository;
import app.services.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultStrategyService implements StrategyService {

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private BollingerBandsStrategyRepository bollingerBandsStrategyRepository;

    @Autowired
    private TwoMovingAveragesStrategyRepository twoMovingAveragesStrategyRepository;

    @Override
    public List<Strategy> getAllStrategies() {
        return strategyRepository.findAll();
    }

    @Override
    public List<BollingerBandsStrategy> getAllBollingerStrategies() { return bollingerBandsStrategyRepository.findAll(); }

    @Override
    public List<TwoMovingAveragesStrategy> getAllMovingAveragesStrategies() {
        return twoMovingAveragesStrategyRepository.findAll();
    }

    @Override
    public List<Strategy> getActiveStrategies() {
        return strategyRepository.findByIsActive(true);
    }

    @Override
    public Strategy getStrategyById(Integer id) {
        return strategyRepository.getOne(id);
    }

    @Override
    public Strategy createStrategy(Strategy strategy) {
        return strategyRepository.save(strategy);
    }

    @Override
    public BollingerBandsStrategy createOrSaveBollingerStrategy(BollingerBandsStrategy bollingerBandsStrategy){
        return bollingerBandsStrategyRepository.save(bollingerBandsStrategy);
    }

    @Override
    public TwoMovingAveragesStrategy createOrSaveMovingAveragesStrategy(TwoMovingAveragesStrategy twoMovingAveragesStrategy){
        return twoMovingAveragesStrategyRepository.save(twoMovingAveragesStrategy);
    }

    @Override
    public Strategy updateStrategy(Strategy strategy) {
        return strategyRepository.save(strategy);
    }

    @Override
    public void deleteStrategyById(Integer id) {
        strategyRepository.deleteById(id);
    }

    @Override
    public void deleteStrategy(Strategy strategy) {
        strategyRepository.delete(strategy);
    }

    @Override
    public List<Trade> getTradesByStrategyId(Integer id) {
        Strategy strategy = strategyRepository.getOne(id);
        return strategy.getTrades();
    }
}

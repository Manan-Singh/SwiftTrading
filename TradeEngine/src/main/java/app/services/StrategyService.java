package app.services;

import app.entities.Trade;
import app.entities.strategies.BollingerBandsStrategy;
import app.entities.strategies.Strategy;
import app.entities.strategies.TwoMovingAveragesStrategy;

import java.util.List;

public interface StrategyService {

    List<Strategy> getAllStrategies();

    List<BollingerBandsStrategy> getAllBollingerStrategies();

    List<TwoMovingAveragesStrategy> getAllMovingAverageStrategies();

    List<Strategy> getActiveStrategies();

    Strategy getStrategyById(Integer id);

    Strategy createStrategy(Strategy strategy);

    BollingerBandsStrategy createOrSaveBollingerStrategy(BollingerBandsStrategy bollingerBandsStrategy);

    TwoMovingAveragesStrategy createOrSaveMovingAveragesStrategy(TwoMovingAveragesStrategy twoMovingAveragesStrategy);

    Strategy updateStrategy(Strategy strategy);

    void deleteStrategyById(Integer id);

    void deleteStrategy(Strategy strategy);

    List<Trade> getTradesByStrategyId(Integer id);
}

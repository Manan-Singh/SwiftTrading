package app.services.implementations;

import app.callables.BollingerCallableStrategy;
import app.callables.CallableStrategy;
import app.callables.TwoMovingAveragesCallableStrategy;
import app.entities.strategies.BollingerBandsStrategy;
import app.entities.strategies.Strategy;
import app.entities.strategies.TwoMovingAveragesStrategy;
import app.services.CallableStrategyContextMapperService;
import app.services.StockPriceRecordService;
import app.services.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class DefaultCallableStrategyContextMapperService implements CallableStrategyContextMapperService {

    @Autowired
    private JmsTemplate template;

    @Autowired
    private StockPriceRecordService stockPriceRecordService;

    @Autowired
    private StrategyService strategyService;

    /**
     * Gives back the appropriate callable strategy based on the one given
     * @param s a strategy entity that contains configurable parameters for the callabe strategy
     * @return the appropriate callable strategy to execute
     */
    public CallableStrategy getCallable(Strategy s) {
        if (s instanceof TwoMovingAveragesStrategy) {
            return new TwoMovingAveragesCallableStrategy(stockPriceRecordService, strategyService, template,
                    (TwoMovingAveragesStrategy) s);
        } else if (s instanceof BollingerBandsStrategy) {
            return  new BollingerCallableStrategy(stockPriceRecordService, strategyService, template,
                    (BollingerBandsStrategy) s);
        }
        return null;
    }
}

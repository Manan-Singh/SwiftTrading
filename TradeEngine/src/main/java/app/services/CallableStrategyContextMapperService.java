package app.services;

import app.contexts.callables.BollingerCallableStrategy;
import app.contexts.callables.CallableStrategy;
import app.contexts.callables.TwoMovingAveragesCallableStrategy;
import app.entities.strategies.BollingerBandsStrategy;
import app.entities.strategies.Strategy;
import app.entities.strategies.TwoMovingAveragesStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class CallableStrategyContextMapperService {

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
        } else if (s instanceof TwoMovingAveragesStrategy) {
            return  new BollingerCallableStrategy(stockPriceRecordService, strategyService, template,
                    (BollingerBandsStrategy) s);
        }
        return null;
    }
}

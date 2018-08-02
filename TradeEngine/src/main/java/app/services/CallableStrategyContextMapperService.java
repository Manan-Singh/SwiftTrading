package app.services;

import app.contexts.BollingerStrategyContext;
import app.contexts.CallableStrategyContext;
import app.contexts.ChaosCallableStrategyContext;
import app.contexts.TwoMovingAveragesCallableStrategyContext;
import app.entities.strategies.BollingerBandsStrategy;
import app.entities.strategies.ChaosStrategy;
import app.entities.strategies.Strategy;
import app.entities.strategies.TwoMovingAveragesStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallableStrategyContextMapperService {

    @Autowired
    private ChaosCallableStrategyContext chaosContext;

    @Autowired
    private BollingerStrategyContext bollingerContext;

    @Autowired
    TwoMovingAveragesCallableStrategyContext movingAvgContext;

    public CallableStrategyContext.CallableStrategy getCallable(Strategy s) {
        if (s instanceof ChaosStrategy) {
            return chaosContext.getCallableStrategy(s);
        } else if (s instanceof TwoMovingAveragesStrategy) {
            return  movingAvgContext.getCallableStrategy(s);
        } else if (s instanceof BollingerBandsStrategy) {
            return bollingerContext.getCallableStrategy(s);
        }
        return null;
    }
}

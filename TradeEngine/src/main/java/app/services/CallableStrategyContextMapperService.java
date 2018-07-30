package app.services;

import app.contexts.CallableStrategyContext;
import app.contexts.ChaosCallableStrategyContext;
import app.entities.strategies.ChaosStrategy;
import app.entities.strategies.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallableStrategyContextMapperService {

    @Autowired
    private ChaosCallableStrategyContext chaosContext;

    public CallableStrategyContext.CallableStrategy getCallable(Strategy s) {
        if (s instanceof ChaosStrategy) {
            return chaosContext.getCallableStrategy(s);
        }
        return null;
    }
}

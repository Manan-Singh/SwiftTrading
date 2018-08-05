package app.services;

import app.callables.CallableStrategy;
import app.entities.strategies.Strategy;

public interface CallableStrategyContextMapperService {

    CallableStrategy getCallable(Strategy s);
}

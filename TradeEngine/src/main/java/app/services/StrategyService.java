package app.services;

import app.entities.strategies.Strategy;

import java.util.List;

public interface StrategyService {

    public List<Strategy> getAllStrategies();

    public List<Strategy> getActiveStrategies();
}

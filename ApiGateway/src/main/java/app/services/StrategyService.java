package app.services;

import app.entities.strategies.Strategy;

import java.util.List;

public interface StrategyService {

    List<Strategy> getAllStrategies();

    List<Strategy> getActiveStrategies();
}

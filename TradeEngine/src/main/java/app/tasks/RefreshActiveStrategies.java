package app.tasks;

import app.entities.strategies.Strategy;
import app.services.CallableStrategyContextMapperService;
import app.services.StrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class RefreshActiveStrategies {

    private static final Logger logger = LoggerFactory.getLogger(RefreshActiveStrategies.class);
    private Map<Strategy, Future<Void>> activeStrategies = new HashMap<>();

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private CallableStrategyContextMapperService strategyContextMapperService;

    /**
     * This method takes care of loading in new active strategies and kicking out strategies that have already
     * finished executing
     */
    @Scheduled(fixedRate = 10000)
    public void refresh() {
        List<Strategy> currentlyActive = strategyService.getActiveStrategies();
        List<Strategy> toExec = getStrategiesToBeExecuted(currentlyActive);
        List<Strategy> toPause = getStrategiesToBePaused(currentlyActive);

        toPause.forEach(x -> {
            Future<Void> task = activeStrategies.get(x);
            task.cancel(true);
            activeStrategies.remove(x);
        });

        toExec.forEach(x -> {
            Callable<Void> task = strategyContextMapperService.getCallable(x);
            Future<Void> future = executorService.submit(task);
            activeStrategies.put(x, future);
        });

        logger.info("Refreshed active strategies");
    }

    private List<Strategy> getStrategiesToBeExecuted(List<Strategy> currentlyActive) {
        return currentlyActive.stream().filter(x -> activeStrategies.get(x) == null).collect(Collectors.toList());
    }

    private List<Strategy> getStrategiesToBePaused(List<Strategy> currentlyActive) {
        return activeStrategies.keySet().stream().filter(x -> !currentlyActive.contains(x)).collect(Collectors.toList());
    }
}

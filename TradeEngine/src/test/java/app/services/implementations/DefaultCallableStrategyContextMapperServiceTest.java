package app.services.implementations;

import app.callables.BollingerCallableStrategy;
import app.callables.TwoMovingAveragesCallableStrategy;
import app.entities.strategies.BollingerBandsStrategy;
import app.entities.strategies.TwoMovingAveragesStrategy;
import app.services.StockPriceRecordService;
import app.services.StrategyService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.Callable;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultCallableStrategyContextMapperServiceTest {

    @Mock
    private JmsTemplate template;

    @Mock
    private StockPriceRecordService stockPriceRecordService;

    @Mock
    private StrategyService strategyService;

    @InjectMocks
    private DefaultCallableStrategyContextMapperService service;

    private TwoMovingAveragesStrategy movingStrategy;
    private BollingerBandsStrategy bollingerStrategy;

    @Before
    public void setUp() {
        initMocks(this);
        service = new DefaultCallableStrategyContextMapperService();
        movingStrategy = new TwoMovingAveragesStrategy();
        bollingerStrategy = new BollingerBandsStrategy();
    }

    @Test
    public void testGetCallable() {
        Callable<Void> callable = service.getCallable(movingStrategy);
        assertTrue(callable instanceof TwoMovingAveragesCallableStrategy);

        callable = service.getCallable(bollingerStrategy);
        assertTrue(callable instanceof BollingerCallableStrategy);
    }

}
package app.callables;

import app.entities.Order;
import app.entities.strategies.Strategy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CallableStrategyTest {

    private Strategy strategy;

    @Before
    public void setUp() {
        strategy = spy(Strategy.class);
        strategy.setTicker("AAPL");
        strategy.setEntrySize(40);
        when(strategy.getId()).thenReturn(2);
    }

    @Test
    public void testGetOrder() {
        Order order = CallableStrategy.getOrder(true, 12.00, strategy);
        assertTrue(order.getStock().equals("AAPL"));
        assertTrue(order.getPrice() == 12.00);
        assertTrue(order.isBuyTrade());
        assertTrue(order.getTradeSize() == 40);
        assertTrue(order.getId() == 2);

        order = CallableStrategy.getOrder(false, 12.00, strategy);
        assertFalse(order.isBuyTrade());
    }

    @Test
    public void testShouldExit() {
        assertTrue(CallableStrategy.shouldExit(100.00, 5, -6.00));
        assertTrue(CallableStrategy.shouldExit(100.00, 5, 15.00));
        assertFalse(CallableStrategy.shouldExit(100.00, 5, -2.00));
        assertFalse(CallableStrategy.shouldExit(100.00, 5, 4.00));
        assertTrue(CallableStrategy.shouldExit(100.00, 5, 5.00));
    }

}
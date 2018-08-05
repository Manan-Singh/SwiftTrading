package app.callables;

import org.junit.Test;

import static org.junit.Assert.*;

public class BollingerCallableStrategyTest {

    @Test
    public void testShouldBuy() {
        assertTrue(BollingerCallableStrategy.shouldBuy(15.00, 20, 0.45, 2));
        assertFalse(BollingerCallableStrategy.shouldBuy(15.00, 17, 1.5, 2));
    }

    @Test
    public void testShouldSell() {
        assertTrue(BollingerCallableStrategy.shouldSell(15.00, 10, 0.45, 2));
        assertFalse(BollingerCallableStrategy.shouldSell(15.00, 13, 1.5, 2));
    }

}
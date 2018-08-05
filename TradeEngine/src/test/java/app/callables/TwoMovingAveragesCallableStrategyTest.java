package app.callables;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class TwoMovingAveragesCallableStrategyTest {

    @Test
    public void testShouldBuy() {
        assertTrue(TwoMovingAveragesCallableStrategy.shouldBuy(50.00, 49.00, 49.00, 50.00));
        assertFalse(TwoMovingAveragesCallableStrategy.shouldBuy(20.00, 30.00, 49.00, 50.00));
    }

    @Test
    public void testShouldSell() {
        assertTrue(TwoMovingAveragesCallableStrategy.shouldSell(49.00, 50.00, 50.00, 49.00));
        assertFalse(TwoMovingAveragesCallableStrategy.shouldSell(30.00, 20.00, 49.00, 50.00));
    }

}
package app.services.implementations;

import app.entities.strategies.Strategy;
import app.repositories.StrategyRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultStrategyServiceTest {

    private DefaultStrategyService strategyService;
    private StrategyRepository strategyRepository;

    @Before
    public void setUp(){

        // use a DefaultStrategyService for testing and mock the StrategyRepository
        strategyService = new DefaultStrategyService();
        strategyRepository = mock(StrategyRepository.class);
        strategyService.setStrategyRepository(strategyRepository);
    }

    @Test
    public void testToggleIsActiveField(){

        // setup
        Strategy strategy3 = new Strategy();
        strategy3.setIsActive(false);
        when(strategyRepository.getOne(3)).thenReturn(strategy3);

        Strategy strategy4 = new Strategy();
        strategy4.setIsActive(true);
        when(strategyRepository.getOne(4)).thenReturn(strategy4);

        Strategy strategy;
        Integer id;

        // -- Test Toggle False To True --

        // arrange
        id = 3;
        // act
        strategyService.toggleIsActiveField(id);
        // assert
        strategy = strategyRepository.getOne(id);
        assertTrue(strategy.getIsActive());

        // -- Test Toggle True To False --

        // arrange
        id = 4;
        // act
        strategyService.toggleIsActiveField(id);
        // assert
        strategy = strategyRepository.getOne(id);
        assertFalse(strategy.getIsActive());

        // -- Test No Strategy For Given Id

        // arrange
        id = 200;
        // act
        strategy = strategyService.toggleIsActiveField(id);
        // assert
        assertNull(strategy);
    }

    @Test
    public void testUpdateStrategy(){

        // setup
        Strategy strategy1 = new Strategy();
        strategy1.setIsActive(false);
        strategy1.setClosePercentage(20);
        strategy1.setEntrySize(30);
        strategy1.setName("Strategy 1");
        when(strategyRepository.getOne(1)).thenReturn(strategy1);

        Strategy strategy2 = new Strategy();
        strategy2.setIsActive(true);
        strategy2.setClosePercentage(5);
        strategy2.setEntrySize(1000);
        strategy2.setName("Strategy 2");
        when(strategyRepository.getOne(2)).thenReturn(strategy2);

        Strategy strategy;
        Integer id;

        // -- Test Update closePercentage And entrySize --

        // arrange
        id = 1;
        Strategy newStrategy1 = new Strategy();
        newStrategy1.setIsActive(false);
        newStrategy1.setClosePercentage(10);
        newStrategy1.setEntrySize(200);
        newStrategy1.setName("Strategy 1");
        // act
        strategyService.updateStrategy(id, newStrategy1);
        // assert
        strategy = strategyRepository.getOne(id);
        assertEquals((Integer) 10, strategy.getClosePercentage());
        assertEquals((Integer) 200, strategy.getEntrySize());

        // -- Test Update isActive And name --

        // arrange
        id = 2;
        Strategy newStrategy2 = new Strategy();
        newStrategy2.setIsActive(false);
        newStrategy2.setClosePercentage(5);
        newStrategy2.setEntrySize(1000);
        newStrategy2.setName("My Test Strategy");
        // act
        strategyService.updateStrategy(id, newStrategy2);
        // assert
        strategy = strategyRepository.getOne(id);
        assertFalse(strategy.getIsActive());
        assertEquals("My Test Strategy", strategy.getName());
    }

}
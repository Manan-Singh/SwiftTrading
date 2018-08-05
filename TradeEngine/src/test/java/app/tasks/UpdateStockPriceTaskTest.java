package app.tasks;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UpdateStockPriceTaskTest {

    private UpdateStockPriceTask task;
    private List<String> stocks;

    @Before
    public void setUp() {
        task = new UpdateStockPriceTask();

        stocks = new ArrayList<>();
        stocks.add("GOOG");
        stocks.add("MSFT");
        stocks.add("AAPL");
        stocks.add("NFLX");
    }

    @Test
    public void testGetMarketQuery() {
        String marketQuery = task.getMarketQuery(stocks);
        assertTrue(marketQuery.equals("nullgoog,msft,aapl,nflx&f=s0l1"));
    }

}
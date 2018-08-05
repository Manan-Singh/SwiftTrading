package app.contexts;

import app.entities.Order;
import app.entities.StockPriceRecord;
import app.entities.strategies.Strategy;
import app.services.StockPriceRecordService;
import app.services.StrategyService;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public abstract class CallableStrategyContext {

    public abstract CallableStrategy getCallableStrategy(Strategy s);

    public abstract class CallableStrategy implements Callable<Void> {

        protected static final int DEFAULT_ENTRY = 50;

        protected StockPriceRecordService stockService;
        protected StrategyService strategyService;

        protected StockPriceRecordService getStockService() {
            return stockService;
        }

        protected void setStockService(StockPriceRecordService stockService) {
            this.stockService = stockService;
        }

        protected StrategyService getStrategyService() {
            return strategyService;
        }

        protected void setStrategyService(StrategyService strategyService) {
            this.strategyService = strategyService;
        }

        protected Order getOrder(boolean buyTrade, double price, int size, Strategy s) {
            Order order = new Order();
            order.setBuyTrade(buyTrade);
            order.setTradeSize(size);
            order.setPrice(price);
            order.setId(s.getId());
            order.setStock(s.getTicker());
            order.setTimeTransacted(LocalDateTime.now().toString());
            return order;
        }


        // checks if the pnl violates the exit condition
        protected boolean shouldExit(double startValue, double exitPercentage, double pnl) {
            double margin = startValue * (exitPercentage / 100);
            if (Math.abs(pnl) >= margin) {
                return true;
            }
            return false;
        }
    }
}

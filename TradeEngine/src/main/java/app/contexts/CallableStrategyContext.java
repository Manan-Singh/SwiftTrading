package app.contexts;

import app.entities.Order;
import app.entities.strategies.Strategy;
import app.services.StockService;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public abstract class CallableStrategyContext {

    public abstract CallableStrategy getCallableStrategy(Strategy s);

    public abstract class CallableStrategy implements Callable<Void> {

        protected StockService stockService;

        protected StockService getStockService() {
            return stockService;
        }

        protected void setStockService(StockService stockService) {
            this.stockService = stockService;
        }

        protected Order getOrder(boolean buyTrade, double price, Strategy s) {
            Order order = new Order();
            order.setBuyTrade(buyTrade);
            if (buyTrade) {
                order.setTradeSize(s.getEntryPosition());
            } else {
                order.setTradeSize(s.getClosePosition());
            }
            order.setPrice(price);
            order.setId(s.getId());
            order.setStock(s.getStock().getTicker());
            order.setTimeTransacted(LocalDateTime.now().toString());
            return order;
        }
    }
}

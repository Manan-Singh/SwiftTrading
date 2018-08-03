package app.contexts.callables;

import app.entities.Order;
import app.entities.strategies.Strategy;
import app.services.StockPriceRecordService;
import app.services.StrategyService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.Callable;

public abstract class CallableStrategy implements Callable<Void> {

    protected StockPriceRecordService stockPriceService;
    protected StrategyService strategyService;
    protected JmsTemplate jmsTemplate;
    protected Queue queue;
    protected XmlMapper xmlMapper = new XmlMapper();
    protected String responseUuid = UUID.randomUUID().toString();

    public static Order getOrder(boolean buyTrade, double price, int size, Strategy s) {
        Order order = new Order();
        order.setBuyTrade(buyTrade);
        order.setTradeSize(size);
        order.setPrice(price);
        order.setId(s.getId());
        order.setStock(s.getTicker());
        order.setTimeTransacted(LocalDateTime.now().toString());
        return order;
    }

    public static boolean shouldExit(double startValue, double exitPercentage, double pnl) {
        double margin = startValue * exitPercentage;
        if (pnl < startValue - margin || pnl > startValue + margin) {
            return true;
        }
        return false;
    }

    public StockPriceRecordService getStockPriceService() {
        return stockPriceService;
    }

    public void setStockPriceService(StockPriceRecordService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    public StrategyService getStrategyService() {
        return strategyService;
    }

    public void setStrategyService(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}


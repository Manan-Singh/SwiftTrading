package app.contexts.callables;

import app.entities.Order;
import app.entities.StockPriceRecord;
import app.entities.strategies.Strategy;
import app.services.StockPriceRecordService;
import app.services.StrategyService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * A strategy that can be executed by an ExecutorService. This means that the strategy can be cancelled and resumed
 */
public abstract class CallableStrategy implements Callable<Void> {

    protected StockPriceRecordService stockPriceService;
    protected StrategyService strategyService;
    protected JmsTemplate jmsTemplate;
    protected XmlMapper xmlMapper = new XmlMapper();
    protected String responseUuid = UUID.randomUUID().toString();
    protected static final int THROTTLE = 3000;
    private static final Logger logger = LoggerFactory.getLogger(CallableStrategy.class);

    public CallableStrategy(StockPriceRecordService sprs, StrategyService ss, JmsTemplate jmsTemplate) {
        this.stockPriceService = sprs;
        this.strategyService = ss;
        this.jmsTemplate = jmsTemplate;
    }

    /**
     * Returns an buy/sell Order object based on the parameters
     * @param buyTrade a boolean representing if this trade is a buy or a sell (true if buy, false if sell)
     * @param price the price that the trade is being transacted at
     * @param s the strategy that is executing the trade
     * @return
     */
    public static Order getOrder(boolean buyTrade, double price, Strategy s) {
        Order order = new Order();
        order.setBuyTrade(buyTrade);
        order.setTradeSize(s.getEntrySize());
        order.setPrice(price);
        order.setId(s.getId());
        order.setStock(s.getTicker());
        order.setTimeTransacted(LocalDateTime.now().toString());
        return order;
    }

    /**
     * @param startValue the initial value of the first transaction of the executing strategy
     * @param exitPercentage the percent margin that the profit and loss can be
     * @param pnl the profit and loss value that needs to be compared to the exit percentage
     * @return a boolean that tells if the strategy should stop running because it has violated the exit condition
     */
    public static boolean shouldExit(double startValue, double exitPercentage, double pnl) {
        double margin = startValue * exitPercentage;
        if (Math.abs(pnl) <= margin || Math.abs(pnl) >= margin) {
            return true;
        }
        return false;
    }

    /**
     * Sends a String message to the OrderBroker
     * @param message the message to send (to the OrderBroker)
     */
    protected void send(String message) {
        try {
            jmsTemplate.send("OrderBroker", (Session session) -> {
                Message msg = session.createTextMessage(message);
                msg.setJMSCorrelationID(responseUuid);
                return msg;
            });
        } catch(Exception e) {
            logger.warn("Could send message");
            e.printStackTrace();
        }
    }

}


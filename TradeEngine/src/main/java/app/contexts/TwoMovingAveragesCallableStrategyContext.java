package app.contexts;

import app.entities.Order;
import app.entities.strategies.Strategy;
import app.entities.strategies.TwoMovingAveragesStrategy;
import app.services.StockPriceRecordService;
import app.services.StrategyService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import java.io.Serializable;
import java.util.UUID;

@Service
public class TwoMovingAveragesCallableStrategyContext extends CallableStrategyContext implements Serializable  {

    @Autowired
    private StockPriceRecordService stockService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    @Override
    public CallableStrategy getCallableStrategy(Strategy s) {
        if (!(s instanceof TwoMovingAveragesStrategy)) {
            throw new IllegalStateException();
        }
        TwoMovingAveragesCallableStrategy callableStrategy = new TwoMovingAveragesCallableStrategy();
        callableStrategy.setTwoMovingAveragesStrategy( ((TwoMovingAveragesStrategy)s) );
        callableStrategy.setStockService(stockService);
        callableStrategy.setStrategyService(strategyService);
        return callableStrategy;
    }

    public class TwoMovingAveragesCallableStrategy extends CallableStrategy implements Serializable {

        private TwoMovingAveragesStrategy strategy;
        private XmlMapper xmlMapper = new XmlMapper();
        private String responseUuid = UUID.randomUUID().toString();

        private TwoMovingAveragesCallableStrategy() {}

        @Override
        public Void call() throws Exception {

            // value of first buy/sell
            double startingValue = 0;
            double exitPercent = strategy.getClosePercentage();
            double check = 0;
            // running pnl since start of strategy
            double pnl = 0;
            // a constantly updated pnl of a buy and sell pair
            double pairPnl = 0;
            double prevLongAvg = 0, prevShortAvg = 0;
            // shows if the current iteration of the loop has to send a close order
            boolean hasToClose = false;

            while (!Thread.currentThread().isInterrupted()) {
                // load up configurable parameters
                int longTime = strategy.getLongTime();
                int shortTime = strategy.getShortTime();

                // find the average price of the records for both the long and short periods
                Double longAvg = stockService.getAverageStockPrice(longTime, strategy.getTicker());
                Double shortAvg = stockService.getAverageStockPrice(shortTime, strategy.getTicker());
                if (longAvg == null || shortAvg == null) {
                    // in case the feed doesn't have any data on prices yet
                    Thread.sleep(longTime);
                    continue;
                }

                double currentPrice = stockService.getMostRecentStockPrice(strategy.getTicker());
                if (startingValue == 0) {
                    startingValue = currentPrice * DEFAULT_ENTRY;
                    check = startingValue * exitPercent;
                }

                Order order = null;

                if (prevLongAvg != 0 && prevShortAvg != 0) {

                    if ( (prevLongAvg - prevShortAvg) < 0 && (longAvg - shortAvg) > 0 ) {
                        // you should sell
                        order = getOrder(false, currentPrice, DEFAULT_ENTRY, strategy);
                        pairPnl += currentPrice * DEFAULT_ENTRY;
                    } else if ( (prevLongAvg - prevShortAvg) > 0  && (longAvg - shortAvg) < 0) {
                        // you should buy
                        order = getOrder(true, currentPrice, DEFAULT_ENTRY, strategy);
                        pairPnl -= currentPrice * DEFAULT_ENTRY;
                    }
                }

                prevLongAvg = longAvg;
                prevShortAvg = shortAvg;

                // send the order to the OrderBroker
                if (order != null) {
                    String xml = xmlMapper.writeValueAsString(order);
                    try {
                        jmsTemplate.send(queue, (Session s) -> {
                            Message m = s.createTextMessage(xml);
                            m.setJMSCorrelationID(responseUuid);
                            return m;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (hasToClose) {
                        // check if exit condition has been violated
                        pnl += pairPnl;
                        if (pnl <= (startingValue - check) || pnl >= (startingValue + check) ) {
                            break;
                        }
                        // reset pair pnl
                        pairPnl = 0;
                    }

                    hasToClose = !hasToClose;

                    // introduce artificial lag to make it not execute trades on the same timestamp
                    Thread.sleep(1000);
                }
            }

            strategy.setIsActive(false);
            strategy.setProfitValue(pnl);
            strategyService.createOrSaveMovingAveragesStrategy(strategy);

            return null;
        }

        public TwoMovingAveragesStrategy getStrategy() {
            return strategy;
        }

        public void setTwoMovingAveragesStrategy(TwoMovingAveragesStrategy strategy) {
            this.strategy = strategy;
        }
    }
}

package app.contexts;

import app.entities.Order;
import app.entities.strategies.BollingerBandsStrategy;
import app.entities.strategies.Strategy;
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
public class BollingerStrategyContext extends CallableStrategyContext implements Serializable {

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
        if (!(s instanceof BollingerBandsStrategy)) {
            return null;
        }
        BollingerCallableStrategy callableStrategy = new BollingerCallableStrategy();
        callableStrategy.setBollingerStrategy( (BollingerBandsStrategy)s );
        callableStrategy.setStockService(stockService);
        callableStrategy.setStrategyService(strategyService);
        return callableStrategy;
    }

    public class BollingerCallableStrategy extends CallableStrategy implements Serializable {

        private BollingerBandsStrategy strategy;
        private XmlMapper xmlMapper = new XmlMapper();
        private String responseUuid = UUID.randomUUID().toString();

        @Override
        public Void call() throws Exception {

            double exitPercentage = strategy.getClosePercentage();
            double startValue = 0;
            // help to ensure that transactions occur in buy/sell pairs
            boolean mustBuy = true, mustSell = true;
            boolean hasToClose = false;
            double pairPnl = 0;
            double runningPnl = 0;
            //TODO: make this multiplier configurable?
            final double DEFAULT_MULTIPLIER = 2.0;

            while (!Thread.currentThread().isInterrupted()) {
                Double movingAvg = stockService.getAverageStockPrice(strategy.getPeriod(), strategy.getTicker());
                Double movingStdDev = stockService.getStdDev(strategy.getPeriod(), strategy.getTicker());
                if (movingAvg == null || movingStdDev == null) {
                    // in case the feed doesn't have any data on prices yet
                    Thread.sleep(1000);
                    continue;
                }

                double currentPrice = stockService.getMostRecentStockPrice(strategy.getTicker());
                if (startValue == 0) {
                    startValue = currentPrice;
                }

                Order order = null;

                if ( currentPrice <= (movingAvg - (movingStdDev * DEFAULT_MULTIPLIER)) && mustBuy ) {
                    // should buy
                    order = getOrder(true, currentPrice, DEFAULT_ENTRY, strategy);
                    pairPnl -= currentPrice;
                    mustBuy = false;
                    mustSell = true;
                } else if( currentPrice >= (movingAvg + (movingStdDev * DEFAULT_MULTIPLIER)) && mustSell) {
                    // should sell
                    order = getOrder(false, currentPrice, DEFAULT_ENTRY, strategy);
                    pairPnl += currentPrice;
                    mustSell = false;
                    mustBuy = true;
                }

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
                        runningPnl += pairPnl;
                        if (shouldExit(startValue, exitPercentage, runningPnl)) {
                            break;
                        }
                        pairPnl = 0;
                    }
                    hasToClose = !hasToClose;

                    // introduce artificial lag to make it not execute trades on the same timestamp
                    Thread.sleep(1000);
                }
            }

            strategy.setIsActive(false);
            strategy.setProfitValue(runningPnl);
            strategyService.createOrSaveBollingerStrategy(strategy);

            return null;
        }

        public BollingerBandsStrategy getBollingerStrategy() {
            return strategy;
        }

        public void setBollingerStrategy(BollingerBandsStrategy strategy) {
            this.strategy = strategy;
        }
    }
}

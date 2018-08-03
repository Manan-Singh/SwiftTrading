package app.contexts.callables;

import app.entities.Order;
import app.entities.strategies.BollingerBandsStrategy;
import app.services.StockPriceRecordService;
import app.services.StrategyService;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

public class BollingerCallableStrategy extends CallableStrategy {

    private BollingerBandsStrategy strategy;

    public BollingerCallableStrategy(StockPriceRecordService sprs, StrategyService ss, JmsTemplate jmsTemplate,
                                     Queue queue, BollingerBandsStrategy s) {
        super(sprs, ss, jmsTemplate, queue);
        this.strategy = s;
    }

    @Override
    public Void call() throws Exception {
        double exitPercentage = strategy.getClosePercentage();
        int tradeSize = strategy.getEntrySize();
        double startValue = 0;
        // help to ensure that transactions occur in buy/sell pairs
        boolean mustBuy = true, mustSell = true;
        boolean hasToClose = false;
        double pairPnl = 0;
        double runningPnl = 0;

        while (!Thread.currentThread().isInterrupted()) {
            Double movingAvg = stockPriceService.getAverageStockPrice(strategy.getPeriod(), strategy.getTicker());
            Double movingStdDev = stockPriceService.getStdDev(strategy.getPeriod(), strategy.getTicker());
            if (movingAvg == null || movingStdDev == null) {
                // in case the feed doesn't have any data on prices yet
                Thread.sleep(1000);
                continue;
            }

            double currentPrice = stockPriceService.getMostRecentStockPrice(strategy.getTicker());
            if (startValue == 0) {
                startValue = currentPrice;
            }

            Order order = null;

            if ( currentPrice <= (movingAvg - (movingStdDev * strategy.getStdDev())) && mustBuy ) {
                // should buy
                order = getOrder(true, currentPrice, strategy);
                pairPnl -= currentPrice;
                mustBuy = false;
                mustSell = true;
            } else if( currentPrice >= (movingAvg + (movingStdDev * strategy.getStdDev())) && mustSell) {
                // should sell
                order = getOrder(false, currentPrice, strategy);
                pairPnl += currentPrice;
                mustSell = false;
                mustBuy = true;
            }

            if (order != null) {
                send(xmlMapper.writeValueAsString(order));

                if (hasToClose) {
                    runningPnl += pairPnl;
                    if (shouldExit(startValue, exitPercentage, runningPnl)) {
                        break;
                    }
                    pairPnl = 0;
                }
                hasToClose = !hasToClose;

            }
            // introduce artificial lag to make it not execute trades on the same timestamp
            Thread.sleep(THROTTLE);
        }

        strategy.setIsActive(false);
        strategy.setProfitValue(runningPnl);
        strategyService.createOrSaveBollingerStrategy(strategy);

        return null;
    }

    public static boolean shouldBuy() {
        return true;
    }

    public static boolean shouldSell() {
        return true;
    }
}

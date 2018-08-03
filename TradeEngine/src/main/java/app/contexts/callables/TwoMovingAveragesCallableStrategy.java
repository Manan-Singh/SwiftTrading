package app.contexts.callables;

import app.entities.Order;
import app.entities.strategies.TwoMovingAveragesStrategy;

import javax.jms.Message;
import javax.jms.Session;

public class TwoMovingAveragesCallableStrategy extends CallableStrategy {


    private TwoMovingAveragesStrategy strategy;

    @Override
    public Void call() throws Exception {

        // value of first buy/sell
        double startingValue = 0;
        int tradeSize = strategy.getEntrySize();
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
            Double longAvg = stockPriceService.getAverageStockPrice(longTime, strategy.getTicker());
            Double shortAvg = stockPriceService.getAverageStockPrice(shortTime, strategy.getTicker());
            if (longAvg == null || shortAvg == null) {
                // in case the feed doesn't have any data on prices yet
                Thread.sleep(longTime);
                continue;
            }

            double currentPrice = stockPriceService.getMostRecentStockPrice(strategy.getTicker());
            if (startingValue == 0) {
                startingValue = currentPrice * tradeSize;
                check = startingValue * exitPercent;
            }

            Order order = null;

            if (prevLongAvg != 0 && prevShortAvg != 0) {

                if ( (prevLongAvg - prevShortAvg) < 0 && (longAvg - shortAvg) > 0 ) {
                    // you should sell
                    order = getOrder(false, currentPrice, tradeSize, strategy);
                    pairPnl += currentPrice * tradeSize;
                } else if ( (prevLongAvg - prevShortAvg) > 0  && (longAvg - shortAvg) < 0) {
                    // you should buy
                    order = getOrder(true, currentPrice, tradeSize, strategy);
                    pairPnl -= currentPrice * tradeSize;
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

    public void setStrategy(TwoMovingAveragesStrategy strategy) {
        this.strategy = strategy;
    }
}

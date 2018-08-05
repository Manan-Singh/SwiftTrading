package app.callables;

import app.entities.Order;
import app.entities.strategies.TwoMovingAveragesStrategy;
import app.services.StockPriceRecordService;
import app.services.StrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 * An implementation of the TwoMovingAverages Market strategy which waits for changes in the
 * long and short average in order to decide when to buy and sell
 */
public class TwoMovingAveragesCallableStrategy extends CallableStrategy {


    private TwoMovingAveragesStrategy strategy;
    private Logger logger = LoggerFactory.getLogger(TwoMovingAveragesCallableStrategy.class);

    public TwoMovingAveragesCallableStrategy(StockPriceRecordService sprs, StrategyService ss,
                                             JmsTemplate jmsTemplate, TwoMovingAveragesStrategy s) {
        super(sprs, ss, jmsTemplate);
        this.strategy = s;
    }

    /**
     *  This is the main method that runs the TwoMovingAverages strategy. If the short average crosses the long average
     *  then the strategy buys. It sells if the opposite happens
     * @return nothing
     * @throws Exception
     */
    @Override
    public Void call() throws Exception {

        // value of the price of the stock when entering the market times the entry size
        double startingValue = 0;
        // the amount of stocks the strategy buys or sells
        int tradeSize = strategy.getEntrySize();
        // represents the point at which a strategy should exit
        double exitPercent = strategy.getClosePercentage();
        // running pnl since start of strategy
        double pnl = 0;
        // a constantly updated pnl of a buy and sell pair
        double pairPnl = 0;
        // the previous long and short averages
        double prevLongAvg = 0, prevShortAvg = 0;
        // shows if the current iteration of the loop has to send a close order
        boolean hasToClose = false;

        while (!Thread.currentThread().isInterrupted()) {
            // load up configurable parameters
            int longTime = strategy.getLongTime();
            int shortTime = strategy.getShortTime();

            // find the average price of the records for both the long and short periods, and get the current price
            Double longAvg = stockPriceService.getAverageStockPrice(longTime, strategy.getTicker());
            Double shortAvg = stockPriceService.getAverageStockPrice(shortTime, strategy.getTicker());
            Double currentPrice = stockPriceService.getMostRecentStockPrice(strategy.getTicker());

            if (currentPrice == null || longAvg == null || shortAvg == null) {
                // in case the feed doesn't have any data on prices yet
                Thread.sleep(longTime);
                continue;
            }
            if (startingValue == 0) {
                startingValue = currentPrice * tradeSize;
            }

            Order order = null;
            if (shouldBuy(prevLongAvg, prevShortAvg, longAvg, shortAvg)) {
                order = getOrder(true, currentPrice, strategy);
                pairPnl -= (currentPrice * tradeSize);
            } else if (shouldSell(prevLongAvg, prevShortAvg, longAvg, shortAvg)) {
                order = getOrder(false, currentPrice, strategy);
                pairPnl += (currentPrice * tradeSize);
            }
            prevLongAvg = longAvg;
            prevShortAvg = shortAvg;

            if (order != null) {
                // send the order to the OrderBroker
                send(xmlMapper.writeValueAsString(order));

                if (hasToClose) {
                    // check if exit condition has been violated
                    pnl += pairPnl;
                    if (shouldExit(startingValue, exitPercent, pnl)) {
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
        strategy.setProfitValue(pnl);
        strategyService.createOrSaveMovingAveragesStrategy(strategy);

        return null;
    }

    /**
     * @param prevLongAvg the long average from the previous iteration of the strategy
     * @param prevShortAvg the short average from the previous iteration of the strategy
     * @param longAvg the current long average
     * @param shortAvg the current short average
     * @return a boolean specifying whether you should buy in the market right now based on the long and short averages
     */
    public static boolean shouldBuy(double prevLongAvg, double prevShortAvg, double longAvg, double shortAvg) {
        if ( (prevShortAvg - prevLongAvg) < 0 && (shortAvg - longAvg) >= 0 ) {
            return true;
        }
        else return false;
    }

    /**
     * @param prevLongAvg the long average from the previous iteration of the strategy
     * @param prevShortAvg the short average from the previous iteration fo the strategy
     * @param longAvg the current long average
     * @param shortAvg the current short average
     * @return a boolean specifying whether you should sell in the market right now based on the long and short averages
     */
    public static boolean shouldSell(double prevLongAvg, double prevShortAvg, double longAvg, double shortAvg) {
        if ( (prevShortAvg - prevLongAvg) >= 0 && (shortAvg - longAvg) < 0 ) {
            return true;
        }
        else return false;
    }

}

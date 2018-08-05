package app.callables;

import app.entities.Order;
import app.entities.strategies.BollingerBandsStrategy;
import app.services.StockPriceRecordService;
import app.services.StrategyService;
import org.springframework.jms.core.JmsTemplate;

/**
 * An implementation of the BollingerBands Market strategy which buys and sell based
 * on the moving average and standard deviation of the products
 */
public class BollingerCallableStrategy extends CallableStrategy {

    private BollingerBandsStrategy strategy;

    public BollingerCallableStrategy(StockPriceRecordService sprs, StrategyService ss, JmsTemplate jmsTemplate,
                                     BollingerBandsStrategy s) {
        super(sprs, ss, jmsTemplate);
        this.strategy = s;
    }

    /**
     *  This is the main method that runs the Bollinger strategy. If the moving average hits some
     *  multiple of the standard deviation, then the strategy will make a trade
     * @return nothing
     * @throws Exception
     */
    @Override
    public Void call() throws Exception {
        // represents the point at which a strategy should exit
        double exitPercentage = strategy.getClosePercentage();
        // the amount of stocks the strategy buys or sells
        int tradeSize = strategy.getEntrySize();
        // value of the price of the stock when entering the market times the entry size
        double startValue = 0;
        // help to ensure that transactions occur in buy/sell pairs
        boolean mustBuy = true, mustSell = true;
        // shows if the current iteration of the loop has to send a close order
        boolean hasToClose = false;
        // a constantly updated pnl of a buy and sell pair
        double pairPnl = 0;
        // running pnl since start of strategy
        double runningPnl = 0;

        while (!Thread.currentThread().isInterrupted()) {
            Double movingAvg = stockPriceService.getAverageStockPrice(strategy.getPeriod(), strategy.getTicker());
            Double movingStdDev = stockPriceService.getStdDev(strategy.getPeriod(), strategy.getTicker());
            Double currentPrice = stockPriceService.getMostRecentStockPrice(strategy.getTicker());

            if (currentPrice == null || movingAvg == null || movingStdDev == null) {
                // in case the feed doesn't have any data on prices yet
                Thread.sleep(1000);
                continue;
            }
            if (startValue == 0) {
                startValue = currentPrice * tradeSize;
            }

            Order order = null;
            if (shouldBuy(currentPrice, movingAvg, movingStdDev, strategy.getStdDev()) && mustBuy) {
                order = getOrder(true, currentPrice, strategy);
                pairPnl -= (currentPrice * tradeSize);
                mustBuy = false;
                mustSell = true;
            } else if (shouldSell(currentPrice, movingAvg, movingStdDev, strategy.getStdDev()) && mustSell) {
                order = getOrder(false, currentPrice, strategy);
                pairPnl += (currentPrice * tradeSize);
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
                    mustBuy = true;
                    mustSell = true;
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

    /**
     * @param currentPrice the current price of the stock
     * @param avg the moving average of the stock for a certain period
     * @param stddev the standard deviation of the stock for a certain period
     * @param multiplier the multiplier of the standard deviation necessary to hit to buy
     * @return
     */
    public static boolean shouldBuy(double currentPrice, double avg, double stddev, double multiplier) {
        if ( currentPrice <= (avg - (stddev * multiplier)) ) {
            return true;
        }
        return false;
    }

    /**
     * @param currentPrice the current price of the stock
     * @param avg the moving average of the stock for a certain period
     * @param stddev the standard deviation of the stock for a certain period
     * @param multiplier the multiplier of the standard deviation necessary to hit to sell
     * @return
     */
    public static boolean shouldSell(double currentPrice, double avg, double stddev, double multiplier) {
        if ( currentPrice >= (avg + (stddev * multiplier)) ) {
            return true;
        }
        return false;
    }
}

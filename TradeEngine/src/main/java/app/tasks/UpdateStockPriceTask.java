package app.tasks;

import app.entities.StockPriceRecord;
import app.services.StockPriceRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/*
    This class periodically updates the BidPrice and AskPrice values of any Stock that belongs to an active strategy
*/

@Component
public class UpdateStockPriceTask {

    private static final String FIELDS = "&f=s0l1";
    private static final Logger logger = LoggerFactory.getLogger(UpdateStockPriceTask.class);
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${market-feed.url}")
    private String marketFeedUrl;

    @Autowired
    private StockPriceRecordService stockService;

    @Scheduled(fixedRate = 500)
    private void updateStockPrices() {
        List<String> active = stockService.getAllActiveStocks();
        if (!active.isEmpty()) {
            String restQuery = getMarketQuery(active);
            String priceString = restTemplate.getForObject(restQuery, String.class);
            String[] prices = priceString.split("\n");
            // the string array and the stock list are guaranteed to be the same size and order
            for (int i = 0; i < prices.length; i++) {
                String ticker = (prices[i].split(",")[0]).toUpperCase();
                ticker = ticker.substring(1, ticker.length()-1);
                Double price = Double.parseDouble(prices[i].split(",")[1]);
                StockPriceRecord spr = new StockPriceRecord();
                spr.setPrice(price);
                spr.setTicker(ticker);
                spr.setTimeInspected(Timestamp.from(Instant.now()));
                stockService.save(spr);
            }
            logger.info("Updated active stock prices");
        }
    }

    // this gives a complete url query that will return the bid and ask prices for each active stock
    private String getMarketQuery(List<String> stocks) {
        String stockString = stocks.stream().collect(Collectors.joining(",")).toLowerCase();
        return marketFeedUrl + stockString + FIELDS;
    }
}

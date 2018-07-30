package app.tasks;

import app.entities.Stock;
import app.services.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
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
    private StockService stockService;

    @Scheduled(fixedRate = 5000)
    private void updateStockPrices() {
        List<Stock> active = stockService.getAllActiveStocks();
        if (!active.isEmpty()) {
            String restQuery = getMarketQuery(active);
            String priceString = restTemplate.getForObject(restQuery, String.class);
            String[] prices = priceString.split("\n");
            // the string array and the stock list are guaranteed to be the same size and order
            for (int i = 0; i < prices.length; i++) {
                Double price = Double.parseDouble(prices[i].split(",")[1]);
                active.get(i).setPrice(price);
                stockService.save(active.get(i));
            }
        }
        logger.info("Updated active stock prices");
    }

    // this gives a complete url query that will return the bid and ask prices for each active stock
    private String getMarketQuery(List<Stock> stocks) {
        String stockString = stocks.stream().map(Stock::getTicker).collect(Collectors.joining(",")).toLowerCase();
        return marketFeedUrl + stockString + FIELDS;
    }
}

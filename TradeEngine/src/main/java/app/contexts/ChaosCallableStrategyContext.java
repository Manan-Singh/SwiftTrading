package app.contexts;

import app.entities.Order;
import app.entities.Stock;
import app.entities.strategies.ChaosStrategy;
import app.entities.strategies.Strategy;
import app.services.StockService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Component
public class ChaosCallableStrategyContext extends CallableStrategyContext {

    @Autowired
    private StockService stockService;

    @Autowired
    private JmsTemplate template;
    @Autowired
    private Queue queue;
    private static String testJMSMessage = "<trade>\n" +
            "<buy>true</buy> <!-- so, false for a sale -->\n" +
            "<id>0</id>\n" +
            "<price>88.0</price>\n" +
            "<size>2000</size>\n" +
            "<stock>HON</stock>\n" +
            "<whenAsDate>2014-07-31T22:33:22.801-04:00</whenAsDate>\n" +
            "</trade>";

    public CallableStrategy getCallableStrategy(Strategy s) {
        if (!(s instanceof ChaosStrategy)) {
            throw new IllegalStateException();
        }
        ChaosCallableStrategy callableStrategy = new ChaosCallableStrategy();
        callableStrategy.setChaosStrategy( ((ChaosStrategy)s) );
        callableStrategy.setStockService(stockService);
        return callableStrategy;
    }

    public class ChaosCallableStrategy extends CallableStrategy {

        private ChaosStrategy strategy;
        private XmlMapper xmlMapper = new XmlMapper();

        private ChaosCallableStrategy() {}

        @Override
        public Void call() throws Exception {
            while (!Thread.currentThread().isInterrupted()) {
                // have to get the stock from the db to get updated prices
                Stock stock = stockService.getStock(strategy.getStock().getTicker());
                double price = stock.getPrice();
                boolean buyTrade = (Math.random() >= .5 ? true : false);

                Order order = null;
                if (buyTrade) {
                    order = getOrder(true, price, strategy);
                } else {
                    order = getOrder(false, price, strategy);
                }

                String xml = xmlMapper.writeValueAsString(order);
                System.out.println("\n" + xml + "\n");
//                try {
//                    template.convertAndSend(queue, testJMSMessage);
//                }
//                catch(Exception e) {e.printStackTrace();}
                Thread.sleep(5000);
            }
            return null;
        }

        ChaosStrategy getChaosStrategy() {
            return strategy;
        }

        void setChaosStrategy(ChaosStrategy strategy) {
            this.strategy = strategy;
        }

    }
}

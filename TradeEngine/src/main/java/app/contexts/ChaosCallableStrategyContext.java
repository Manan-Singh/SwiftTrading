package app.contexts;

import app.entities.Order;
import app.entities.strategies.ChaosStrategy;
import app.entities.strategies.Strategy;
import app.services.StockPriceRecordService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import java.io.Serializable;
import java.util.UUID;

@Component
public class ChaosCallableStrategyContext extends CallableStrategyContext implements Serializable  {

    @Autowired
    private StockPriceRecordService stockService;

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

    public class ChaosCallableStrategy extends CallableStrategy implements Serializable {

        private ChaosStrategy strategy;
        private XmlMapper xmlMapper = new XmlMapper();
        private String responseUuid = UUID.randomUUID().toString();

        private ChaosCallableStrategy() {}

        @Override
        public Void call() throws Exception {
            while (!Thread.currentThread().isInterrupted()) {
                // get the long and short moving averages
                double price = stockService.getMostRecentStockPrice(strategy.getTicker());
                boolean buyTrade = (Math.random() >= .5 ? true : false);

                Order order = null;
                if (buyTrade) {
                    order = getOrder(true, price, DEFAULT_ENTRY, strategy);
                } else {
                    order = getOrder(false, price, DEFAULT_ENTRY, strategy);
                }

                String xml = xmlMapper.writeValueAsString(order);
                System.out.println("\n" + xml + "\n");
                try {
                    //sendMessage(xml);
                    template.send(queue, (Session session) -> {
                        Message msg = session.createTextMessage(xml);
                        msg.setJMSCorrelationID(responseUuid);
                        return msg;
                    });
                }
                catch(Exception e) {e.printStackTrace();}
                Thread.sleep(10000);
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

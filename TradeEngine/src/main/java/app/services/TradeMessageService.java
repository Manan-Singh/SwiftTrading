package app.services;

import app.entities.Trade;
import app.entities.TradeDTO;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class TradeMessageService {

    private static Logger logger = LoggerFactory.getLogger(TradeMessageService.class);
    private XmlMapper xmlMapper = new XmlMapper();

    @Autowired
    private TradeService tradeService;

    @JmsListener(destination = "OrderBroker_Reply")
    public void listenAndHandleTrades(String message) {
        try {
            TradeDTO tradeDTO = xmlMapper.readValue(message, TradeDTO.class);
            if (tradeDTO.getState() == null || tradeDTO.getState().isEmpty()) {
                tradeDTO.setState("FILLED");
            }
            tradeDTO.setTimeTransacted(tradeDTO.getTimeTransacted().substring(0, tradeDTO.getTimeTransacted().lastIndexOf('-')));
            tradeService.saveTrade(tradeDTO.isBuyTrade(), tradeDTO.getId(), tradeDTO.getPrice(),
                    tradeDTO.getTradeSize(), tradeDTO.getTimeTransacted(), tradeDTO.getState());
        } catch (Exception e) {
            logger.warn("JMS Message could not be read");
            e.printStackTrace();
            return;
        }

    }
}

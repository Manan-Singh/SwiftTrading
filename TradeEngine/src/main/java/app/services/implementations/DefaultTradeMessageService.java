package app.services.implementations;

import app.entities.TradeDTO;
import app.services.TradeMessageServcie;
import app.services.TradeService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Service
public class DefaultTradeMessageService implements TradeMessageServcie {

    private static Logger logger = LoggerFactory.getLogger(DefaultTradeMessageService.class);
    private XmlMapper xmlMapper = new XmlMapper();

    @Autowired
    private TradeService tradeService;

    /**
     * Listens for trades from the OrderBroker and persists them into the DB
     * @param message the string representation of a trade from the OrderBroker
     */
    @JmsListener(destination = "OrderBroker_Reply")
    public void listenAndHandleTrades(String message) {
        logger.info("Receiving trade");
        TradeDTO tradeDTO = getTradeDTO(message);
        if (tradeDTO != null) {
            tradeService.saveTrade(tradeDTO.isBuyTrade(), tradeDTO.getId(), tradeDTO.getPrice(),
                    tradeDTO.getTradeSize(), tradeDTO.getTimeTransacted(), tradeDTO.getState());
        } else {
            logger.warn("JMS Message could not be read");
        }
    }

    /**
     * Deserializes a TradeDTO object from a string of xml
     * @param message the xml representation of the trade
     * @return a TradeDTO object with the properties of the xml message passed in
     */
    public TradeDTO getTradeDTO(String message) {
        TradeDTO tradeDTO = null;
        try {
            tradeDTO = xmlMapper.readValue(message, TradeDTO.class);
        } catch (Exception e) {
            return null;
        }
        if (tradeDTO.getState() == null || tradeDTO.getState().isEmpty()) {
            tradeDTO.setState("FILLED");
        }
        tradeDTO.setTimeTransacted(getProperTimeString(tradeDTO));
        return tradeDTO;
    }

    /**
     * Corrects the string of a TradeDTO object into a string representing a datetime that can be stored in the DB
     * @param tradeDTO the TradeDTO object's datetime to read from
     * @return the proper datetime string representation, will make sure it's not a ZonedDateTime representation
     */
    private String getProperTimeString(TradeDTO tradeDTO) {
        LocalDateTime time = null;
        try {
            ZonedDateTime zoned = ZonedDateTime.parse(tradeDTO.getTimeTransacted());
            time = zoned.toLocalDateTime();
        } catch(Exception e) {
            time = LocalDateTime.parse(tradeDTO.getTimeTransacted());
        }
        return time.toString();
    }
}

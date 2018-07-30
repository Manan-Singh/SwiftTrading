package app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class TradeMessageService {

    private static Logger logger = LoggerFactory.getLogger(TradeMessageService.class);

    @JmsListener(destination = "OrderBroker_Reply")
    public void listenAndHandleTrades(String message) {
        logger.info(message);
    }
}

package app.services;

import app.entities.TradeDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TradeMessageServiceTest {

    private TradeMessageService messageService;

    @Before
    public void setUp() {
        messageService = new TradeMessageService();
    }

    @Test
    public void testGetTradeDTO() {
        String testMessage = "Blah Blah Blah";
        assertNull(messageService.getTradeDTO(testMessage));

        testMessage = "<trade>\n" +
                "<buy>true</buy>\n" +
                "<id>0</id>\n" +
                "<price>88.0</price>\n" +
                "<size>2000</size>\n" +
                "<stock>HON</stock>\n" +
                "<whenAsDate>2014-07-31T22:33:22.801-04:00</whenAsDate>\n" +
                "<result>PARTIALLY_FILLED</result>\n" +
                "</trade>";
        TradeDTO tradeDTO = messageService.getTradeDTO(testMessage);
        assertNotNull(tradeDTO);
        assertTrue(tradeDTO.getPrice() == 88.0);
        assertTrue(tradeDTO.getId() == 0);
        assertTrue(tradeDTO.getStock().equals("HON"));
        assertTrue(tradeDTO.getTradeSize() == 2000);
        assertTrue(tradeDTO.getTimeTransacted().equals("2014-07-31T22:33:22.801"));
        assertTrue(tradeDTO.getState().equals("PARTIALLY_FILLED"));
    }

}
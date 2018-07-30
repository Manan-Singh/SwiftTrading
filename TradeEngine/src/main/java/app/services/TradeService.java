package app.services;

public interface TradeService {

    public void saveTrade(boolean buy, int strategyId, double price, int tradeSize, String ticker, String time, String state);
}

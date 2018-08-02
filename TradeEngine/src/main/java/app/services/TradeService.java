package app.services;

public interface TradeService {

    public void saveTrade(boolean buy, int strategyId, double price, int tradeSize, String time, String state);
}

USE swift;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Strategies;
TRUNCATE TABLE ChaosStrategies;
TRUNCATE TABLE TwoMovingAveragesStrategies;
TRUNCATE TABLE Trades;
TRUNCATE TABLE Stocks;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO Stocks (Ticker, Price) VALUES ('GOOG', 0);
INSERT INTO Stocks (Ticker, Price) VALUES ('FB', 0);
INSERT INTO Stocks (Ticker, Price) VALUES ('AAPL', 0);
INSERT INTO Stocks (Ticker, Price) VALUES ('MSFT', 0);
INSERT INTO Stocks (Ticker, Price) VALUES ('NSC', 0);
INSERT INTO Stocks (Ticker, Price) VALUES ('BRK-A', 0);

INSERT INTO Strategies (Entry, Close, Stock, StrategyType, Name) VALUES (100, 50, 'GOOG', 'Bollinger', 'Test Strategy');
INSERT INTO ChaosStrategies (Id) VALUES (1);

INSERT INTO Trades (Buy, StrategyId, Price, TradeSize, Stock, TransactionState, TimeTransacted) VALUES (0, 1, 205.30, 50, 'GOOG', 'FILLED', NOW());
INSERT INTO Trades (Buy, StrategyId, Price, TradeSize, Stock, TransactionState, TimeTransacted) VALUES (0, 1, 195.30, 50, 'GOOG', 'FILLED', NOW());
INSERT INTO Trades (Buy, StrategyId, Price, TradeSize, Stock, TransactionState, TimeTransacted) VALUES (0, 1, 200, 50, 'GOOG', 'FILLED', NOW());

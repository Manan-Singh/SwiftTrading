USE swift;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Strategies;
TRUNCATE TABLE ChaosStrategies;
TRUNCATE TABLE TwoMovingAveragesStrategies;
TRUNCATE TABLE Trades;
TRUNCATE TABLE StockPriceRecords;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO StockPriceRecords (Ticker, TimeInspected, Price) VALUES ('GOOG', NOW(), 0);
INSERT INTO StockPriceRecords (Ticker, TimeInspected, Price) VALUES ('TSLA', NOW(), 0);
INSERT INTO StockPriceRecords (Ticker, TimeInspected, Price) VALUES ('AAPL', NOW(), 0);
INSERT INTO StockPriceRecords (Ticker, TimeInspected, Price) VALUES ('MSFT', NOW(), 0);

INSERT INTO Strategies (IsActive, Name, Close, Ticker) VALUES (1, "Chris", 100, 'GOOG');
INSERT INTO Strategies (IsActive, Name, Close, Ticker) VALUES (0, "Chris2", 100, 'TSLA');
INSERT INTO Strategies (IsActive, Name, Close, Ticker) VALUES (0, "Manan's", 5, 'MSFT');
INSERT INTO Strategies (IsActive, Name, Close, Ticker) VALUES (0, "Manan2", 10, 'GOOG');
INSERT INTO Strategies (IsActive, Name, Close, Ticker) VALUES (0, "Manan3", 25, 'TSLA');
INSERT INTO Strategies (IsActive, Name, Close, Ticker) VALUES (0, "Manan4", 8, 'MSFT');
INSERT INTO Strategies (IsActive, Name, Close, Ticker) VALUES (0, "Manan5", 25, 'AAPL');

INSERT INTO ChaosStrategies (Id) VALUES (1);
INSERT INTO ChaosStrategies (Id) VALUES (2);
INSERT INTO TwoMovingAveragesStrategies (Id, LongTime, ShortTime) VALUES (3, 60, 30);
INSERT INTO TwoMovingAveragesStrategies (Id, LongTime, ShortTime) VALUES (4, 280, 120);
INSERT INTO TwoMovingAveragesStrategies (Id, LongTime, ShortTime) VALUES (5, 120, 60);
INSERT INTO BollingerBandsStrategies (Id, Period, StdDev) VALUES (6, 30, 2);
INSERT INTO BollingerBandsStrategies (Id, Period, StdDev) VALUES (7, 30, 1.2);

INSERT INTO Trades (Buy, StrategyId, Price, TradeSize, TransactionState, TimeTransacted) VALUES (0, 1, 205.30, 50, 'FILLED', NOW());
INSERT INTO Trades (Buy, StrategyId, Price, TradeSize, TransactionState, TimeTransacted) VALUES (0, 1, 195.30, 50, 'FILLED', NOW());
INSERT INTO Trades (Buy, StrategyId, Price, TradeSize, TransactionState, TimeTransacted) VALUES (0, 1, 200, 50, 'FILLED', NOW());

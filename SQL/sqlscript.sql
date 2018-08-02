

CREATE TABLE StockPriceRecords (
    Id INTEGER NOT NULL AUTO_INCREMENT,
    Ticker VARCHAR(10) NOT NULL,
    TimeInspected TIMESTAMP NOT NULL,
    Price FLOAT NOT NULL,
    PRIMARY KEY (Id)
);

CREATE TABLE Strategies (
    Id INTEGER NOT NULL AUTO_INCREMENT,
    IsActive BOOLEAN DEFAULT TRUE,
    Name VARCHAR(50) NOT NULL,
    Close INTEGER NOT NULL,
    EntrySize INTEGER NOT NULL DEFAULT 50,
    Ticker VARCHAR(10) NOT NULL,
    ProfitValue FLOAT,
    PRIMARY KEY (Id)
);

-- For testing purposes
CREATE TABLE ChaosStrategies (
    Id INTEGER NOT NULL,
    FOREIGN KEY (Id) REFERENCES Strategies (Id),
    PRIMARY KEY (Id)
);

-- LongTime & ShortTime are in SECONDS
CREATE TABLE TwoMovingAveragesStrategies (
    Id INTEGER NOT NULL,
    LongTime INTEGER NOT NULL,
    ShortTime INTEGER NOT NULL,
    FOREIGN KEY (Id) REFERENCES Strategies (Id),
    PRIMARY KEY (Id)
);

CREATE TABLE BollingerBandsStrategies (
    Id INTEGER NOT NULL,
    Period INTEGER NOT NULL,
    StdDev FLOAT NOT NULL,
    FOREIGN KEY (Id) REFERENCES Strategies (Id),
    PRIMARY KEY (Id)
);

CREATE TABLE Trades (
    Id INTEGER NOT NULL AUTO_INCREMENT,
    Buy BOOLEAN NOT NULL,
    StrategyId INTEGER NOT NULL,
    Price FLOAT NOT NULL,
    TradeSize INTEGER NOT NULL,
    TimeTransacted DATETIME NOT NULL,
    TransactionState ENUM('FILLED', 'PARTIALLY_FILLED', 'REJECTED') NOT NULL,
    FOREIGN KEY (StrategyId) REFERENCES Strategies(Id),
    PRIMARY KEY (Id)
);

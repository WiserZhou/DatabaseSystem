-- 1. Team（球队）表
CREATE TABLE Team (
                      TeamID INT PRIMARY KEY,                    -- 主键约束
                      TeamName VARCHAR(100) NOT NULL UNIQUE,     -- 非空约束，唯一约束
                      FoundYear INT,
                      HomeStadiumID INT UNIQUE,                 -- 唯一约束（一对一关系）
                      FOREIGN KEY (HomeStadiumID) REFERENCES Stadium(StadiumID)  -- 外键约束
);

-- 2. Player（球员）表
CREATE TABLE Player (
                        PlayerID INT PRIMARY KEY,                  -- 主键约束
                        Name VARCHAR(100) NOT NULL,                -- 非空约束
                        Age INT CHECK (Age >= 16 AND Age <= 45),   -- 检查约束
                        Position VARCHAR(50) NOT NULL,             -- 非空约束
                        Nationality VARCHAR(50) NOT NULL,          -- 非空约束
                        TeamID INT,                                -- 允许为空（自由球员）
                        FOREIGN KEY (TeamID) REFERENCES Team(TeamID)  -- 外键约束
);

-- 3. Coach（教练）表
CREATE TABLE Coach (
                       CoachID INT PRIMARY KEY,                   -- 主键约束
                       Name VARCHAR(100) NOT NULL,                -- 非空约束
                       Age INT CHECK (Age >= 25),                 -- 检查约束
                       Nationality VARCHAR(50) NOT NULL,          -- 非空约束
                       LicenseLevel VARCHAR(20) NOT NULL,         -- 非空约束
                       TeamID INT UNIQUE,                         -- 唯一约束（一对一关系）
                       FOREIGN KEY (TeamID) REFERENCES Team(TeamID)  -- 外键约束
);

-- 4. Stadium（球场）表
CREATE TABLE Stadium (
                         StadiumID INT PRIMARY KEY,                 -- 主键约束
                         Name VARCHAR(100) NOT NULL UNIQUE,         -- 非空约束，唯一约束
                         Capacity INT NOT NULL,                     -- 非空约束
                         Location VARCHAR(200) NOT NULL,            -- 非空约束
                         BuildYear INT
);

-- 5. Match（比赛）表
CREATE TABLE Match (
                       MatchID INT PRIMARY KEY,                   -- 主键约束
                       HomeTeamID INT NOT NULL,                   -- 非空约束
                       AwayTeamID INT NOT NULL,                   -- 非空约束
                       Date DATE NOT NULL,                        -- 非空约束
                       Time TIME NOT NULL,                        -- 非空约束
                       StadiumID INT NOT NULL,                    -- 非空约束
                       Status VARCHAR(20) DEFAULT 'Scheduled',     -- 默认值约束
                       FOREIGN KEY (HomeTeamID) REFERENCES Team(TeamID),  -- 外键约束
                       FOREIGN KEY (AwayTeamID) REFERENCES Team(TeamID),  -- 外键约束
                       FOREIGN KEY (StadiumID) REFERENCES Stadium(StadiumID),  -- 外键约束
                       CHECK (HomeTeamID != AwayTeamID)           -- 检查约束
);

-- 6. Sponsor（赞助商）表
CREATE TABLE Sponsor (
                         SponsorID INT PRIMARY KEY,                 -- 主键约束
                         Name VARCHAR(100) NOT NULL UNIQUE,         -- 非空约束，唯一约束
                         Industry VARCHAR(100) NOT NULL,            -- 非空约束
                         ContactPerson VARCHAR(100) NOT NULL        -- 非空约束
);

-- 7. Contract（合同）表
CREATE TABLE Contract (
                          ContractID INT PRIMARY KEY,                -- 主键约束
                          PlayerID INT NOT NULL,                     -- 非空约束
                          TeamID INT NOT NULL,                       -- 非空约束
                          StartDate DATE NOT NULL,                   -- 非空约束
                          EndDate DATE NOT NULL,                     -- 非空约束
                          Value DECIMAL(15,2) NOT NULL,              -- 非空约束
                          Status VARCHAR(20) DEFAULT 'Active',        -- 默认值约束
                          FOREIGN KEY (PlayerID) REFERENCES Player(PlayerID),  -- 外键约束
                          FOREIGN KEY (TeamID) REFERENCES Team(TeamID),        -- 外键约束
                          CHECK (StartDate < EndDate)                -- 检查约束
);

-- 8. TeamSponsor（球队赞助关系）表 - 用于实现多对多关系
CREATE TABLE TeamSponsor (
                             TeamID INT,
                             SponsorID INT,
                             StartDate DATE NOT NULL,                   -- 非空约束
                             EndDate DATE,
                             Amount DECIMAL(15,2) NOT NULL,             -- 非空约束
                             PRIMARY KEY (TeamID, SponsorID),           -- 联合主键约束
                             FOREIGN KEY (TeamID) REFERENCES Team(TeamID),        -- 外键约束
                             FOREIGN KEY (SponsorID) REFERENCES Sponsor(SponsorID) -- 外键约束
);
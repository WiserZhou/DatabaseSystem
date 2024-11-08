CREATE TABLE Stadium (
    StadiumID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL UNIQUE,
    Capacity INT NOT NULL,
    Location VARCHAR(200) NOT NULL,
    BuildYear INT
);


CREATE TABLE Team (
    TeamID INT PRIMARY KEY,
    TeamName VARCHAR(100) NOT NULL UNIQUE,
    FoundYear INT,
    HomeStadiumID INT UNIQUE
);


CREATE TABLE Player (
    PlayerID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Age INT CHECK (Age >= 16 AND Age <= 45),
    Position VARCHAR(50) NOT NULL,
    Nationality VARCHAR(50) NOT NULL,
    TeamID INT
);


CREATE TABLE Coach (
    CoachID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Age INT CHECK (Age >= 25),
    Nationality VARCHAR(50) NOT NULL,
    LicenseLevel VARCHAR(20) NOT NULL,
    TeamID INT UNIQUE
);


CREATE TABLE FootballMatch (
    MatchID INT PRIMARY KEY,
    HomeTeamID INT NOT NULL,
    AwayTeamID INT NOT NULL,
    Date DATE NOT NULL,
    Time TIME NOT NULL,
    StadiumID INT NOT NULL,
    Status VARCHAR(20) DEFAULT 'Scheduled',
    CHECK (HomeTeamID != AwayTeamID)
);


CREATE TABLE Sponsor (
    SponsorID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL UNIQUE,
    Industry VARCHAR(100) NOT NULL,
    ContactPerson VARCHAR(100) NOT NULL
);


CREATE TABLE Contract (
    ContractID INT PRIMARY KEY,
    PlayerID INT NOT NULL,
    TeamID INT NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    Value DECIMAL(15,2) NOT NULL,
    Status VARCHAR(20) DEFAULT 'Active',
    CHECK (StartDate < EndDate)
);


CREATE TABLE TeamSponsor (
    TeamID INT,
    SponsorID INT,
    StartDate DATE NOT NULL,
    EndDate DATE,
    Amount DECIMAL(15,2) NOT NULL,
    PRIMARY KEY (TeamID, SponsorID)
);



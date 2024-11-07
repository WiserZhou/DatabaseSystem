ALTER TABLE Team
    ADD CONSTRAINT FK_Team_Stadium
        FOREIGN KEY (HomeStadiumID) REFERENCES Stadium(StadiumID);

ALTER TABLE Player
    ADD CONSTRAINT FK_Player_Team
        FOREIGN KEY (TeamID) REFERENCES Team(TeamID);

ALTER TABLE Coach
    ADD CONSTRAINT FK_Coach_Team
        FOREIGN KEY (TeamID) REFERENCES Team(TeamID);

ALTER TABLE FootballMatch
    ADD CONSTRAINT FK_Match_HomeTeam
        FOREIGN KEY (HomeTeamID) REFERENCES Team(TeamID);

ALTER TABLE FootballMatch
    ADD CONSTRAINT FK_Match_AwayTeam
        FOREIGN KEY (AwayTeamID) REFERENCES Team(TeamID);

ALTER TABLE FootballMatch
    ADD CONSTRAINT FK_Match_Stadium
        FOREIGN KEY (StadiumID) REFERENCES Stadium(StadiumID);

ALTER TABLE Contract
    ADD CONSTRAINT FK_Contract_Player
        FOREIGN KEY (PlayerID) REFERENCES Player(PlayerID);

ALTER TABLE Contract
    ADD CONSTRAINT FK_Contract_Team
        FOREIGN KEY (TeamID) REFERENCES Team(TeamID);

ALTER TABLE TeamSponsor
    ADD CONSTRAINT FK_TeamSponsor_Team
        FOREIGN KEY (TeamID) REFERENCES Team(TeamID);

ALTER TABLE TeamSponsor
    ADD CONSTRAINT FK_TeamSponsor_Sponsor
        FOREIGN KEY (SponsorID) REFERENCES Sponsor(SponsorID);
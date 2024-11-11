# 对常用查询建立视图
CREATE VIEW TeamStadiumInfo AS
SELECT
    Team.TeamName,
    Team.FoundYear,
    Stadium.Name AS StadiumName,
    Stadium.Capacity,
    Stadium.Location
FROM
    Team
        JOIN
    Stadium ON Team.HomeStadiumID = Stadium.StadiumID;

CREATE VIEW PlayerTeamInfo AS
SELECT
    Player.Name AS PlayerName,
    Player.Age,
    Player.Position,
    Player.Nationality,
    Team.TeamName
FROM
    Player
        JOIN
    Team ON Player.TeamID = Team.TeamID;

CREATE VIEW MatchDetails AS
SELECT
    FootballMatch.MatchID,
    HomeTeam.TeamName AS HomeTeam,
    AwayTeam.TeamName AS AwayTeam,
    FootballMatch.Date,
    FootballMatch.Time,
    Stadium.Name AS StadiumName,
    FootballMatch.Status
FROM
    FootballMatch
        JOIN
    Team AS HomeTeam ON FootballMatch.HomeTeamID = HomeTeam.TeamID
        JOIN
    Team AS AwayTeam ON FootballMatch.AwayTeamID = AwayTeam.TeamID
        JOIN
    Stadium ON FootballMatch.StadiumID = Stadium.StadiumID;

CREATE VIEW TeamSponsorInfo AS
SELECT
    Team.TeamName,
    Sponsor.Name AS SponsorName,
    TeamSponsor.StartDate,
    TeamSponsor.EndDate,
    TeamSponsor.Amount
FROM
    TeamSponsor
        JOIN
    Team ON TeamSponsor.TeamID = Team.TeamID
        JOIN
    Sponsor ON TeamSponsor.SponsorID = Sponsor.SponsorID;

# 对常用属性建立索引
CREATE INDEX idx_player_name ON Player(Name);

CREATE INDEX idx_team_name ON Team(TeamName);

CREATE INDEX idx_stadium_name ON Stadium(Name);

CREATE INDEX idx_match_date ON FootballMatch(Date);

CREATE INDEX idx_sponsor_name ON Sponsor(Name);

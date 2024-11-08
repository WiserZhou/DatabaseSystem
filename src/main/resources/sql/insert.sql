INSERT INTO Stadium (StadiumID, Name, Capacity, Location, BuildYear) VALUES
                                                                         (1, 'Camp Nou', 99354, 'Barcelona, Spain', 1957),
                                                                         (2, 'Santiago Bernabeu', 81044, 'Madrid, Spain', 1947),
                                                                         (3, 'Old Trafford', 74879, 'Manchester, England', 1910);


INSERT INTO Team (TeamID, TeamName, FoundYear, HomeStadiumID) VALUES
                                                                  (1, 'FC Barcelona', 1899, 1),
                                                                  (2, 'Real Madrid', 1902, 2),
                                                                  (3, 'Manchester United', 1878, 3);


INSERT INTO Player (PlayerID, Name, Age, Position, Nationality, TeamID) VALUES
                                                                            (1, 'Lionel Messi', 36, 'Forward', 'Argentina', 1),
                                                                            (2, 'Cristiano Ronaldo', 39, 'Forward', 'Portugal', 2),
                                                                            (3, 'Marcus Rashford', 26, 'Forward', 'England', 3),
                                                                            (4, 'Gerard Pique', 37, 'Defender', 'Spain', 1),
                                                                            (5, 'Karim Benzema', 36, 'Forward', 'France', 2);


INSERT INTO Coach (CoachID, Name, Age, Nationality, LicenseLevel, TeamID) VALUES
                                                                              (1, 'Xavi Hernandez', 44, 'Spain', 'UEFA Pro', 1),
                                                                              (2, 'Carlo Ancelotti', 65, 'Italy', 'UEFA Pro', 2),
                                                                              (3, 'Erik ten Hag', 54, 'Netherlands', 'UEFA Pro', 3);


INSERT INTO FootballMatch (MatchID, HomeTeamID, AwayTeamID, Date, Time, StadiumID, Status) VALUES
                                                                                               (1, 1, 2, '2024-11-15', '20:45:00', 1, 'Scheduled'),
                                                                                               (2, 2, 3, '2024-11-22', '18:30:00', 2, 'Scheduled'),
                                                                                               (3, 3, 1, '2024-11-30', '17:00:00', 3, 'Scheduled');


INSERT INTO Sponsor (SponsorID, Name, Industry, ContactPerson) VALUES
                                                                   (1, 'Nike', 'Sportswear', 'John Doe'),
                                                                   (2, 'Adidas', 'Sportswear', 'Jane Smith'),
                                                                   (3, 'Emirates', 'Airline', 'Ahmed Khalifa');


INSERT INTO Contract (ContractID, PlayerID, TeamID, StartDate, EndDate, Value, Status) VALUES
                                                                                           (1, 1, 1, '2023-07-01', '2025-06-30', 50000000.00, 'Active'),
                                                                                           (2, 2, 2, '2023-07-01', '2025-06-30', 60000000.00, 'Active'),
                                                                                           (3, 3, 3, '2022-08-01', '2024-07-31', 40000000.00, 'Active');


INSERT INTO TeamSponsor (TeamID, SponsorID, StartDate, EndDate, Amount) VALUES
                                                                            (1, 1, '2024-01-01', '2026-12-31', 15000000.00),
                                                                            (2, 2, '2023-01-01', '2025-12-31', 20000000.00),
                                                                            (3, 3, '2023-06-01', '2025-05-31', 10000000.00);

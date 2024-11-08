CREATE TRIGGER before_team_delete
BEFORE DELETE ON Team
FOR EACH ROW
BEGIN
    IF EXISTS (SELECT 1 FROM FootballMatch WHERE HomeTeamID = OLD.TeamID OR AwayTeamID = OLD.TeamID) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Cannot delete team with existing matches';
    END IF;

    IF EXISTS (SELECT 1 FROM Contract WHERE TeamID = OLD.TeamID) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete team with existing contracts';
    END IF;
END; 
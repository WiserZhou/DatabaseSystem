START TRANSACTION;

IF EXISTS (SELECT 1 FROM Team WHERE TeamID = @teamID OR TeamName = @teamName) THEN
    SIGNAL SQLSTATE '23000'
    SET MESSAGE_TEXT = 'Duplicate TeamID or TeamName';
    ROLLBACK;
END IF;

IF NOT EXISTS (SELECT 1 FROM Stadium WHERE StadiumID = @homeStadiumID) THEN
    SIGNAL SQLSTATE '23000'
    SET MESSAGE_TEXT = 'Invalid HomeStadiumID. Stadium does not exist';
    ROLLBACK;
END IF;

INSERT INTO Team (TeamID, TeamName, FoundYear, HomeStadiumID) 
VALUES (@teamID, @teamName, @foundYear, @homeStadiumID);

COMMIT;
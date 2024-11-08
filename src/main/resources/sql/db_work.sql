/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80037
 Source Host           : localhost:3306
 Source Schema         : db_work

 Target Server Type    : MySQL
 Target Server Version : 80037
 File Encoding         : 65001

 Date: 07/11/2024 23:32:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for coach
-- ----------------------------
DROP TABLE IF EXISTS `coach`;
CREATE TABLE `coach`  (
  `CoachID` int NOT NULL,
  `Name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Age` int NULL DEFAULT NULL,
  `Nationality` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `LicenseLevel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TeamID` int NULL DEFAULT NULL,
  PRIMARY KEY (`CoachID`) USING BTREE,
  UNIQUE INDEX `TeamID`(`TeamID` ASC) USING BTREE,
  CONSTRAINT `FK_Coach_Team` FOREIGN KEY (`TeamID`) REFERENCES `team` (`TeamID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `coach_chk_1` CHECK (`Age` >= 25)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coach
-- ----------------------------
INSERT INTO `coach` VALUES (1, 'Xavi Hernandez', 44, 'Spain', 'UEFA Pro', 1);
INSERT INTO `coach` VALUES (2, 'Carlo Ancelotti', 65, 'Italy', 'UEFA Pro', 2);
INSERT INTO `coach` VALUES (3, 'Erik ten Hag', 54, 'Netherlands', 'UEFA Pro', 3);

-- ----------------------------
-- Table structure for contract
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract`  (
  `ContractID` int NOT NULL,
  `PlayerID` int NOT NULL,
  `TeamID` int NOT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date NOT NULL,
  `Value` decimal(15, 2) NOT NULL,
  `Status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'Active',
  PRIMARY KEY (`ContractID`) USING BTREE,
  INDEX `FK_Contract_Player`(`PlayerID` ASC) USING BTREE,
  INDEX `FK_Contract_Team`(`TeamID` ASC) USING BTREE,
  CONSTRAINT `FK_Contract_Player` FOREIGN KEY (`PlayerID`) REFERENCES `player` (`PlayerID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Contract_Team` FOREIGN KEY (`TeamID`) REFERENCES `team` (`TeamID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `contract_chk_1` CHECK (`StartDate` < `EndDate`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES (1, 1, 1, '2023-07-01', '2025-06-30', 50000000.00, 'Active');
INSERT INTO `contract` VALUES (2, 2, 2, '2023-07-01', '2025-06-30', 60000000.00, 'Active');
INSERT INTO `contract` VALUES (3, 3, 3, '2022-08-01', '2024-07-31', 40000000.00, 'Active');

-- ----------------------------
-- Table structure for footballmatch
-- ----------------------------
DROP TABLE IF EXISTS `footballmatch`;
CREATE TABLE `footballmatch`  (
  `MatchID` int NOT NULL,
  `HomeTeamID` int NOT NULL,
  `AwayTeamID` int NOT NULL,
  `Date` date NOT NULL,
  `Time` time NOT NULL,
  `StadiumID` int NOT NULL,
  `Status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'Scheduled',
  PRIMARY KEY (`MatchID`) USING BTREE,
  INDEX `FK_Match_HomeTeam`(`HomeTeamID` ASC) USING BTREE,
  INDEX `FK_Match_AwayTeam`(`AwayTeamID` ASC) USING BTREE,
  INDEX `FK_Match_Stadium`(`StadiumID` ASC) USING BTREE,
  INDEX `idx_match_date`(`Date` ASC) USING BTREE,
  CONSTRAINT `FK_Match_AwayTeam` FOREIGN KEY (`AwayTeamID`) REFERENCES `team` (`TeamID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Match_HomeTeam` FOREIGN KEY (`HomeTeamID`) REFERENCES `team` (`TeamID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Match_Stadium` FOREIGN KEY (`StadiumID`) REFERENCES `stadium` (`StadiumID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `footballmatch_chk_1` CHECK (`HomeTeamID` <> `AwayTeamID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of footballmatch
-- ----------------------------
INSERT INTO `footballmatch` VALUES (1, 1, 2, '2024-11-15', '20:45:00', 1, 'Scheduled');
INSERT INTO `footballmatch` VALUES (2, 2, 3, '2024-11-22', '18:30:00', 2, 'Scheduled');
INSERT INTO `footballmatch` VALUES (3, 3, 1, '2024-11-30', '17:00:00', 3, 'Scheduled');

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player`  (
  `PlayerID` int NOT NULL,
  `Name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Age` int NULL DEFAULT NULL,
  `Position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Nationality` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TeamID` int NULL DEFAULT NULL,
  PRIMARY KEY (`PlayerID`) USING BTREE,
  INDEX `FK_Player_Team`(`TeamID` ASC) USING BTREE,
  INDEX `idx_player_name`(`Name` ASC) USING BTREE,
  CONSTRAINT `FK_Player_Team` FOREIGN KEY (`TeamID`) REFERENCES `team` (`TeamID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `player_chk_1` CHECK ((`Age` >= 16) and (`Age` <= 45))
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of player
-- ----------------------------
INSERT INTO `player` VALUES (1, 'Lionel Messi', 36, 'Forward', 'Argentina', 1);
INSERT INTO `player` VALUES (2, 'Cristiano Ronaldo', 39, 'Forward', 'Portugal', 2);
INSERT INTO `player` VALUES (3, 'Marcus Rashford', 26, 'Forward', 'England', 3);
INSERT INTO `player` VALUES (4, 'Gerard Pique', 37, 'Defender', 'Spain', 1);
INSERT INTO `player` VALUES (5, 'Karim Benzema', 36, 'Forward', 'France', 2);

-- ----------------------------
-- Table structure for sponsor
-- ----------------------------
DROP TABLE IF EXISTS `sponsor`;
CREATE TABLE `sponsor`  (
  `SponsorID` int NOT NULL,
  `Name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Industry` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ContactPerson` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`SponsorID`) USING BTREE,
  UNIQUE INDEX `Name`(`Name` ASC) USING BTREE,
  INDEX `idx_sponsor_name`(`Name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sponsor
-- ----------------------------
INSERT INTO `sponsor` VALUES (1, 'Nike', 'Sportswear', 'John Doe');
INSERT INTO `sponsor` VALUES (2, 'Adidas', 'Sportswear', 'Jane Smith');
INSERT INTO `sponsor` VALUES (3, 'Emirates', 'Airline', 'Ahmed Khalifa');

-- ----------------------------
-- Table structure for stadium
-- ----------------------------
DROP TABLE IF EXISTS `stadium`;
CREATE TABLE `stadium`  (
  `StadiumID` int NOT NULL,
  `Name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Capacity` int NOT NULL,
  `Location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `BuildYear` int NULL DEFAULT NULL,
  PRIMARY KEY (`StadiumID`) USING BTREE,
  UNIQUE INDEX `Name`(`Name` ASC) USING BTREE,
  INDEX `idx_stadium_name`(`Name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stadium
-- ----------------------------
INSERT INTO `stadium` VALUES (1, 'Camp Nou', 99354, 'Barcelona, Spain', 1957);
INSERT INTO `stadium` VALUES (2, 'Santiago Bernabeu', 81044, 'Madrid, Spain', 1947);
INSERT INTO `stadium` VALUES (3, 'Old Trafford', 74879, 'Manchester, England', 1910);

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team`  (
  `TeamID` int NOT NULL,
  `TeamName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `FoundYear` int NULL DEFAULT NULL,
  `HomeStadiumID` int NULL DEFAULT NULL,
  PRIMARY KEY (`TeamID`) USING BTREE,
  UNIQUE INDEX `TeamName`(`TeamName` ASC) USING BTREE,
  UNIQUE INDEX `HomeStadiumID`(`HomeStadiumID` ASC) USING BTREE,
  INDEX `idx_team_name`(`TeamName` ASC) USING BTREE,
  CONSTRAINT `FK_Team_Stadium` FOREIGN KEY (`HomeStadiumID`) REFERENCES `stadium` (`StadiumID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of team
-- ----------------------------
INSERT INTO `team` VALUES (1, 'FC Barcelona', 1899, 1);
INSERT INTO `team` VALUES (2, 'Real Madrid', 1902, 2);
INSERT INTO `team` VALUES (3, 'Manchester United', 1878, 3);

-- ----------------------------
-- Table structure for teamsponsor
-- ----------------------------
DROP TABLE IF EXISTS `teamsponsor`;
CREATE TABLE `teamsponsor`  (
  `TeamID` int NOT NULL,
  `SponsorID` int NOT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date NULL DEFAULT NULL,
  `Amount` decimal(15, 2) NOT NULL,
  PRIMARY KEY (`TeamID`, `SponsorID`) USING BTREE,
  INDEX `FK_TeamSponsor_Sponsor`(`SponsorID` ASC) USING BTREE,
  CONSTRAINT `FK_TeamSponsor_Sponsor` FOREIGN KEY (`SponsorID`) REFERENCES `sponsor` (`SponsorID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_TeamSponsor_Team` FOREIGN KEY (`TeamID`) REFERENCES `team` (`TeamID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teamsponsor
-- ----------------------------
INSERT INTO `teamsponsor` VALUES (1, 1, '2024-01-01', '2026-12-31', 15000000.00);
INSERT INTO `teamsponsor` VALUES (2, 2, '2023-01-01', '2025-12-31', 20000000.00);
INSERT INTO `teamsponsor` VALUES (3, 3, '2023-06-01', '2025-05-31', 10000000.00);

-- ----------------------------
-- View structure for matchdetails
-- ----------------------------
DROP VIEW IF EXISTS `matchdetails`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `matchdetails` AS select `footballmatch`.`MatchID` AS `MatchID`,`hometeam`.`TeamName` AS `HomeTeam`,`awayteam`.`TeamName` AS `AwayTeam`,`footballmatch`.`Date` AS `Date`,`footballmatch`.`Time` AS `Time`,`stadium`.`Name` AS `StadiumName`,`footballmatch`.`Status` AS `Status` from (((`footballmatch` join `team` `hometeam` on((`footballmatch`.`HomeTeamID` = `hometeam`.`TeamID`))) join `team` `awayteam` on((`footballmatch`.`AwayTeamID` = `awayteam`.`TeamID`))) join `stadium` on((`footballmatch`.`StadiumID` = `stadium`.`StadiumID`)));

-- ----------------------------
-- View structure for playerteaminfo
-- ----------------------------
DROP VIEW IF EXISTS `playerteaminfo`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `playerteaminfo` AS select `player`.`Name` AS `PlayerName`,`player`.`Age` AS `Age`,`player`.`Position` AS `Position`,`player`.`Nationality` AS `Nationality`,`team`.`TeamName` AS `TeamName` from (`player` join `team` on((`player`.`TeamID` = `team`.`TeamID`)));

-- ----------------------------
-- View structure for teamsponsorinfo
-- ----------------------------
DROP VIEW IF EXISTS `teamsponsorinfo`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `teamsponsorinfo` AS select `team`.`TeamName` AS `TeamName`,`sponsor`.`Name` AS `SponsorName`,`teamsponsor`.`StartDate` AS `StartDate`,`teamsponsor`.`EndDate` AS `EndDate`,`teamsponsor`.`Amount` AS `Amount` from ((`teamsponsor` join `team` on((`teamsponsor`.`TeamID` = `team`.`TeamID`))) join `sponsor` on((`teamsponsor`.`SponsorID` = `sponsor`.`SponsorID`)));

-- ----------------------------
-- View structure for teamstadiuminfo
-- ----------------------------
DROP VIEW IF EXISTS `teamstadiuminfo`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `teamstadiuminfo` AS select `team`.`TeamName` AS `TeamName`,`team`.`FoundYear` AS `FoundYear`,`stadium`.`Name` AS `StadiumName`,`stadium`.`Capacity` AS `Capacity`,`stadium`.`Location` AS `Location` from (`team` join `stadium` on((`team`.`HomeStadiumID` = `stadium`.`StadiumID`)));

-- ----------------------------
-- Triggers structure for table team
-- ----------------------------
DROP TRIGGER IF EXISTS `before_team_delete`;
delimiter ;;
CREATE TRIGGER `before_team_delete` BEFORE DELETE ON `team` FOR EACH ROW BEGIN
    -- 检查是否有相关的比赛记录
    IF EXISTS (SELECT 1 FROM FootballMatch WHERE HomeTeamID = OLD.TeamID OR AwayTeamID = OLD.TeamID) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Cannot delete team with existing matches';
    END IF;
    
    -- 检查是否有相关的合同
    IF EXISTS (SELECT 1 FROM Contract WHERE TeamID = OLD.TeamID) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete team with existing contracts';
    END IF;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;

package org.example.test3;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class FootballMatchOperations {

    public static void main(String[] args) {
        // 插入操作示例
        // insertMatch(4, 1, 3, LocalDate.of(2024, 12, 1), 
        //            LocalTime.of(20, 45), 1, "Scheduled");

        // 删除操作示例
        // deleteMatch(4);

        // 连接查询操作
        fetchMatchDetails();

        // 嵌套查询操作
        fetchMatchesByStadium("Camp Nou");

        // 分组查询操作
        groupMatchesByStatus();
    }

    // 插入FootballMatch记录
    public static void insertMatch(int matchID, int homeTeamID, int awayTeamID,
                                 LocalDate date, LocalTime time, 
                                 int stadiumID, String status) {
        String sql = "INSERT INTO FootballMatch (MatchID, HomeTeamID, AwayTeamID, Date, Time, StadiumID, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, matchID);
            pstmt.setInt(2, homeTeamID);
            pstmt.setInt(3, awayTeamID);
            pstmt.setDate(4, Date.valueOf(date));
            pstmt.setTime(5, Time.valueOf(time));
            pstmt.setInt(6, stadiumID);
            pstmt.setString(7, status);

            try {
                pstmt.executeUpdate();
                System.out.println("Match inserted successfully.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    System.out.println("Error: Duplicate MatchID.");
                } else if (e.getErrorCode() == 1048) {
                    System.out.println("Error: One of the required fields is missing.");
                } else if (e.getMessage().contains("foreign key constraint")) {
                    System.out.println("Error: Invalid TeamID or StadiumID, foreign key constraint failed.");
                } else if (e.getMessage().contains("CHECK constraint")) {
                    System.out.println("Error: Home team and away team cannot be the same.");
                } else {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除FootballMatch记录
    public static void deleteMatch(int matchID) {
        String sql = "DELETE FROM FootballMatch WHERE MatchID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, matchID);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Match deleted successfully.");
            } else {
                System.out.println("Error: Match with MatchID " + matchID + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 连接查询：查询所有比赛详细信息
    public static void fetchMatchDetails() {
        String sql = "SELECT m.MatchID, ht.TeamName as HomeTeam, at.TeamName as AwayTeam, " +
                    "s.Name as StadiumName, m.Date, m.Time, m.Status " +
                    "FROM FootballMatch m " +
                    "JOIN Team ht ON m.HomeTeamID = ht.TeamID " +
                    "JOIN Team at ON m.AwayTeamID = at.TeamID " +
                    "JOIN Stadium s ON m.StadiumID = s.StadiumID";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Match Details");
            System.out.println("-------------");
            while (rs.next()) {
                System.out.printf("ID: %d | %s vs %s | Stadium: %s | Date: %s | Time: %s | Status: %s%n",
                    rs.getInt("MatchID"),
                    rs.getString("HomeTeam"),
                    rs.getString("AwayTeam"),
                    rs.getString("StadiumName"),
                    rs.getDate("Date"),
                    rs.getTime("Time"),
                    rs.getString("Status"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 嵌套查询：查询指定球场的所有比赛
    public static void fetchMatchesByStadium(String stadiumName) {
        String sql = "SELECT m.MatchID, ht.TeamName as HomeTeam, at.TeamName as AwayTeam, " +
                    "m.Date, m.Time, m.Status " +
                    "FROM FootballMatch m " +
                    "JOIN Team ht ON m.HomeTeamID = ht.TeamID " +
                    "JOIN Team at ON m.AwayTeamID = at.TeamID " +
                    "WHERE m.StadiumID = (SELECT StadiumID FROM Stadium WHERE Name = ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, stadiumName);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Matches at " + stadiumName);
            System.out.println("-------------------------");
            while (rs.next()) {
                System.out.printf("Match ID: %d | %s vs %s | Date: %s | Time: %s | Status: %s%n",
                    rs.getInt("MatchID"),
                    rs.getString("HomeTeam"),
                    rs.getString("AwayTeam"),
                    rs.getDate("Date"),
                    rs.getTime("Time"),
                    rs.getString("Status"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 分组查询：按照比赛状态分组统计
    public static void groupMatchesByStatus() {
        String sql = "SELECT Status, COUNT(*) AS MatchCount " +
                    "FROM FootballMatch " +
                    "GROUP BY Status";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Match Statistics by Status");
            System.out.println("-------------------------");
            while (rs.next()) {
                System.out.printf("Status: %s | Count: %d%n",
                    rs.getString("Status"),
                    rs.getInt("MatchCount"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 
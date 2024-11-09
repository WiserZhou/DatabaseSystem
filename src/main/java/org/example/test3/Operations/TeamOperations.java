package org.example.test3.Operations;

import org.example.test3.utils.DatabaseConnection;
import org.example.test3.utils.DatabaseTransaction;

import java.sql.*;

public class TeamOperations {

    public static void main(String[] args) {
        // 插入操作示例
        // insertTeam(4, "Liverpool FC", 1892, 4);

        // 删除操作示例
        //  deleteTeam(1);

        // 合并球队示例：将ID为1的球队合并到ID为2的球队
         mergeTeams(1, 2);
        
        // 连接查询操作
        fetchTeamDetails();

        // 嵌套查询操作
        fetchTeamsByFoundYear(1900);

        // 分组查询操作
        groupTeamsByStadiumLocation();

    }

    // 插入Team记录
    public static void insertTeam(int teamID, String teamName, int foundYear, int homeStadiumID) {
        String sql = "INSERT INTO Team (TeamID, TeamName, FoundYear, HomeStadiumID) VALUES (?, ?, ?, ?)";

        try {
            // 开始事务
            DatabaseTransaction.beginTransaction();

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, teamID);
                pstmt.setString(2, teamName);
                pstmt.setInt(3, foundYear);
                pstmt.setInt(4, homeStadiumID);

                pstmt.executeUpdate();
                System.out.println("Team inserted successfully.");

                // 提交事务
                DatabaseTransaction.commitTransaction();
            } catch (SQLException e) {
                // 回滚事务
                DatabaseTransaction.rollbackTransaction();

                if (e.getErrorCode() == 1062) {
                    System.out.println("Error: Duplicate TeamID or TeamName Or  HomeStadiumID");
                } else if (e.getErrorCode() == 1452) {
                    System.out.println("Error: Invalid HomeStadiumID. Stadium does not exist.");
                } else {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除Team记录
    public static void deleteTeam(int teamID) {
        String sql = "DELETE FROM Team WHERE TeamID = ?";

        try {
            // 开始事务
            DatabaseTransaction.beginTransaction();

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, teamID);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Team deleted successfully.");
                    // 提交事务
                    DatabaseTransaction.commitTransaction();
                } else {
                    System.out.println("Error: Team with TeamID " + teamID + " not found.");
                    // 回滚事务
                    DatabaseTransaction.rollbackTransaction();
                }
            }
        } catch (SQLException e) {
            // 回滚事务
            DatabaseTransaction.rollbackTransaction();

            if (e.getSQLState().equals("45000")) {
                System.out.println("Error: " + e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
    }

    // 连接查询：查询所有球队及其主场和教练信息
    public static void fetchTeamDetails() {
        String sql = "SELECT t.TeamID, t.TeamName, t.FoundYear, " +
                    "s.Name as StadiumName, s.Capacity, s.Location, " +
                    "c.Name as CoachName, c.Nationality as CoachNationality " +
                    "FROM Team t " +
                    "LEFT JOIN Stadium s ON t.HomeStadiumID = s.StadiumID " +
                    "LEFT JOIN Coach c ON t.TeamID = c.TeamID";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Team Details");
            System.out.println("------------");
            while (rs.next()) {
                System.out.printf("Team: %s | Founded: %d | Stadium: %s | Capacity: %d | Location: %s | Coach: %s (%s)%n",
                    rs.getString("TeamName"),
                    rs.getInt("FoundYear"),
                    rs.getString("StadiumName"),
                    rs.getInt("Capacity"),
                    rs.getString("Location"),
                    rs.getString("CoachName") != null ? rs.getString("CoachName") : "No coach",
                    rs.getString("CoachNationality") != null ? rs.getString("CoachNationality") : "-");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 嵌套查询：查询成立年份早于指定年份的球队及其球员数量
    public static void fetchTeamsByFoundYear(int maxFoundYear) {
        String sql = "SELECT t.*, " +
                    "(SELECT COUNT(*) FROM Player p WHERE p.TeamID = t.TeamID) as PlayerCount " +
                    "FROM Team t " +
                    "WHERE t.FoundYear <= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maxFoundYear);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Teams founded before " + maxFoundYear);
            System.out.println("-------------------------");
            while (rs.next()) {
                System.out.printf("Team: %s | Founded: %d | Players: %d%n",
                    rs.getString("TeamName"),
                    rs.getInt("FoundYear"),
                    rs.getInt("PlayerCount"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 分组查询：按照球场所在地区统计球队
    public static void groupTeamsByStadiumLocation() {
        String sql = "SELECT " +
                    "SUBSTRING_INDEX(s.Location, ',', -1) as Country, " +
                    "COUNT(t.TeamID) as TeamCount, " +
                    "GROUP_CONCAT(t.TeamName) as Teams " +
                    "FROM Team t " +
                    "JOIN Stadium s ON t.HomeStadiumID = s.StadiumID " +
                    "GROUP BY SUBSTRING_INDEX(s.Location, ',', -1)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Teams by Country");
            System.out.println("---------------");
            while (rs.next()) {
                System.out.printf("Country: %s | Number of Teams: %d | Teams: %s%n",
                    rs.getString("Country").trim(),
                    rs.getInt("TeamCount"),
                    rs.getString("Teams"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 球队合并操作
    public static void mergeTeams(int sourceTeamId, int targetTeamId) {
        Connection conn = null;

        try {
            // 开始事务
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. 转移球员到目标球队
            String updatePlayersSql = "UPDATE Player SET TeamID = ? WHERE TeamID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updatePlayersSql)) {
                pstmt.setInt(1, targetTeamId);
                pstmt.setInt(2, sourceTeamId);
                pstmt.executeUpdate();
            }

            // 2. 撤职源球队的教练
            String removeCoachSql = "UPDATE Coach SET TeamID = NULL WHERE TeamID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(removeCoachSql)) {
                pstmt.setInt(1, sourceTeamId);
                pstmt.executeUpdate();
            }

            // 3. 合并赞助商合同
            String mergeSponsorsSql = "UPDATE TeamSponsor SET TeamID = ? WHERE TeamID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(mergeSponsorsSql)) {
                pstmt.setInt(1, targetTeamId);
                pstmt.setInt(2, sourceTeamId);
                pstmt.executeUpdate();
            }

            // 4. 删除旧球队
            String deleteTeamSql = "DELETE FROM Team WHERE TeamID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteTeamSql)) {
                pstmt.setInt(1, sourceTeamId);
                pstmt.executeUpdate();
            }

            // 提交事务
            conn.commit();
            System.out.println("Teams merged successfully!");

        } catch (SQLException e) {
            // 回滚事务
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("Error during team merge: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 恢复自动提交并关闭连接
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

} 
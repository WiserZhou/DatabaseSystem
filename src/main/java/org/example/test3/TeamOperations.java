package org.example.test3;

import java.math.BigDecimal;
import java.sql.*;

public class TeamOperations {

    public static void main(String[] args) {
        // 插入操作示例
        // insertTeam(4, "Liverpool FC", 1892, 4);

        // 删除操作示例
        // deleteTeam(4);

        // 连接查询操作
        fetchTeamDetails();

        // 嵌套查询操作
        fetchTeamsByFoundYear(1900);

        // 分组查询操作
        groupTeamsByStadiumLocation();

        // 转让球队示例：转让ID为1的球队
        handleTeamTransfer(
            1,                          // teamId
            "New Barcelona FC",         // 新球队名称
            4,                          // 新球场ID
            new BigDecimal("500000000") // 转让金额
        );
    }

    // 插入Team记录
    public static void insertTeam(int teamID, String teamName, int foundYear, int homeStadiumID) {
        String sql = "INSERT INTO Team (TeamID, TeamName, FoundYear, HomeStadiumID) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamID);
            pstmt.setString(2, teamName);
            pstmt.setInt(3, foundYear);
            pstmt.setInt(4, homeStadiumID);

            try {
                pstmt.executeUpdate();
                System.out.println("Team inserted successfully.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    System.out.println("Error: Duplicate TeamID or TeamName.");
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

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamID);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Team deleted successfully.");
            } else {
                System.out.println("Error: Team with TeamID " + teamID + " not found.");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key constraint")) {
                System.out.println("Error: Cannot delete team as it has associated players, matches, or contracts.");
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

    /**
     * 处理球队转让事务，包括：
     * 1. 更新球队信息
     * 2. 转移所有球员合同
     * 3. 更新教练信息
     * 4. 处理赞助商关系
     */
    public static void handleTeamTransfer(int teamId, String newTeamName, int newStadiumId, BigDecimal transferAmount) {
        Connection conn = null;
        
        try {
            // 1. 开始事务
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // 2. 检查新球场是否存在且未被占用
            String checkStadiumSql = "SELECT COUNT(*) FROM Team WHERE HomeStadiumID = ? AND TeamID != ?";
            try (PreparedStatement pstmt = conn.prepareStatement(checkStadiumSql)) {
                pstmt.setInt(1, newStadiumId);
                pstmt.setInt(2, teamId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Stadium is already occupied by another team");
                }
            }
            
            // 3. 更新球队信息
            String updateTeamSql = "UPDATE Team SET TeamName = ?, HomeStadiumID = ? WHERE TeamID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateTeamSql)) {
                pstmt.setString(1, newTeamName);
                pstmt.setInt(2, newStadiumId);
                pstmt.setInt(3, teamId);
                int affected = pstmt.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Team not found");
                }
            }
            
            // 4. 更新现有合同状态
            String updateContractsSql = "UPDATE Contract SET Status = 'Transfer Pending' WHERE TeamID = ? AND Status = 'Active'";
            try (PreparedStatement pstmt = conn.prepareStatement(updateContractsSql)) {
                pstmt.setInt(1, teamId);
                pstmt.executeUpdate();
            }
            
            // 5. 处理教练合同
            String updateCoachSql = "UPDATE Coach SET TeamID = NULL WHERE TeamID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateCoachSql)) {
                pstmt.setInt(1, teamId);
                pstmt.executeUpdate();
            }
            
            // 6. 更新赞助商关系
            String updateSponsorsSql = "UPDATE TeamSponsor SET EndDate = CURRENT_DATE WHERE TeamID = ? AND EndDate > CURRENT_DATE";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSponsorsSql)) {
                pstmt.setInt(1, teamId);
                pstmt.executeUpdate();
            }
            
            // 7. 记录转让交易
            String insertTransferSql = "INSERT INTO TeamTransferHistory (TeamID, OldTeamName, NewTeamName, TransferDate, TransferAmount) " +
                                     "VALUES (?, (SELECT TeamName FROM Team WHERE TeamID = ?), ?, CURRENT_DATE, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertTransferSql)) {
                pstmt.setInt(1, teamId);
                pstmt.setInt(2, teamId);
                pstmt.setString(3, newTeamName);
                pstmt.setBigDecimal(4, transferAmount);
                pstmt.executeUpdate();
            }
            
            // 8. 提交事务
            conn.commit();
            System.out.println("Team transfer completed successfully!");
            
        } catch (SQLException e) {
            // 9. 发生错误时回滚事务
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Team transfer failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 10. 恢复自动提交并关闭连接
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

} 
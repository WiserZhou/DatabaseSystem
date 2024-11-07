package org.example.test3;

import java.sql.*;
import java.math.BigDecimal;

public class TeamSponsorOperations {

    public static void main(String[] args) {
        // 插入操作示例
        // insertTeamSponsor(4, 1, "2024-01-01", "2026-12-31", new BigDecimal("5000000.00"));

        // 删除操作示例
        // deleteTeamSponsor(4, 1);

        // 连接查询操作
        fetchTeamSponsorDetails();

        // 嵌套查询操作
        fetchSponsorsByAmount(new BigDecimal("15000000.00"));

        // 分组查询操作
        groupSponsorsByIndustry();
    }

    // 插入TeamSponsor记录
    public static void insertTeamSponsor(int teamID, int sponsorID, String startDate, String endDate, BigDecimal amount) {
        String sql = "INSERT INTO TeamSponsor (TeamID, SponsorID, StartDate, EndDate, Amount) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamID);
            pstmt.setInt(2, sponsorID);
            pstmt.setDate(3, Date.valueOf(startDate));
            pstmt.setDate(4, Date.valueOf(endDate));
            pstmt.setBigDecimal(5, amount);

            try {
                pstmt.executeUpdate();
                System.out.println("Team sponsor relationship inserted successfully.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    System.out.println("Error: This team-sponsor relationship already exists.");
                } else if (e.getErrorCode() == 1452) {
                    System.out.println("Error: Invalid TeamID or SponsorID.");
                } else {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除TeamSponsor记录
    public static void deleteTeamSponsor(int teamID, int sponsorID) {
        String sql = "DELETE FROM TeamSponsor WHERE TeamID = ? AND SponsorID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamID);
            pstmt.setInt(2, sponsorID);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Team sponsor relationship deleted successfully.");
            } else {
                System.out.println("Error: Team sponsor relationship not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 连接查询：查询所有赞助关系详细信息
    public static void fetchTeamSponsorDetails() {
        String sql = "SELECT ts.*, t.TeamName, s.Name as SponsorName, s.Industry " +
                    "FROM TeamSponsor ts " +
                    "JOIN Team t ON ts.TeamID = t.TeamID " +
                    "JOIN Sponsor s ON ts.SponsorID = s.SponsorID";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Team Sponsor Details");
            System.out.println("--------------------");
            while (rs.next()) {
                System.out.printf("Team: %s | Sponsor: %s | Industry: %s | Amount: %.2f | Period: %s to %s%n",
                    rs.getString("TeamName"),
                    rs.getString("SponsorName"),
                    rs.getString("Industry"),
                    rs.getBigDecimal("Amount"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 嵌套查询：查询赞助金额大于指定金额的赞助关系
    public static void fetchSponsorsByAmount(BigDecimal minAmount) {
        String sql = "SELECT t.TeamName, s.Name as SponsorName, ts.Amount " +
                    "FROM TeamSponsor ts " +
                    "JOIN Team t ON ts.TeamID = t.TeamID " +
                    "JOIN Sponsor s ON ts.SponsorID = s.SponsorID " +
                    "WHERE ts.Amount >= ? " +
                    "ORDER BY ts.Amount DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, minAmount);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Sponsorships above " + minAmount);
            System.out.println("-------------------------");
            while (rs.next()) {
                System.out.printf("Team: %s | Sponsor: %s | Amount: %.2f%n",
                    rs.getString("TeamName"),
                    rs.getString("SponsorName"),
                    rs.getBigDecimal("Amount"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 分组查询：按照赞助商行业统计赞助情况
    public static void groupSponsorsByIndustry() {
        String sql = "SELECT s.Industry, " +
                    "COUNT(ts.TeamID) as SponsorshipCount, " +
                    "SUM(ts.Amount) as TotalAmount, " +
                    "GROUP_CONCAT(DISTINCT t.TeamName) as Teams " +
                    "FROM TeamSponsor ts " +
                    "JOIN Team t ON ts.TeamID = t.TeamID " +
                    "JOIN Sponsor s ON ts.SponsorID = s.SponsorID " +
                    "GROUP BY s.Industry";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Sponsorships by Industry");
            System.out.println("-----------------------");
            while (rs.next()) {
                System.out.printf("Industry: %s | Number of Sponsorships: %d | Total Amount: %.2f | Teams: %s%n",
                    rs.getString("Industry"),
                    rs.getInt("SponsorshipCount"),
                    rs.getBigDecimal("TotalAmount"),
                    rs.getString("Teams"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 
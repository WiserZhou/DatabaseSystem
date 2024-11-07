package org.example.test3;

import java.sql.*;

public class SponsorOperations {

    public static void main(String[] args) {
        // 插入操作示例
        // insertSponsor(4, "Pepsi", "Beverages", "Mike Johnson");

        // 删除操作示例
        // deleteSponsor(4);

        // 连接查询操作
        fetchSponsorDetails();

        // 嵌套查询操作
        fetchSponsorsByIndustry("Sportswear");

        // 分组查询操作
        groupSponsorsByIndustry();
    }

    // 插入Sponsor记录
    public static void insertSponsor(int sponsorID, String name, String industry, String contactPerson) {
        String sql = "INSERT INTO Sponsor (SponsorID, Name, Industry, ContactPerson) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sponsorID);
            pstmt.setString(2, name);
            pstmt.setString(3, industry);
            pstmt.setString(4, contactPerson);

            try {
                pstmt.executeUpdate();
                System.out.println("Sponsor inserted successfully.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    System.out.println("Error: Duplicate SponsorID or Name.");
                } else if (e.getErrorCode() == 1048) {
                    System.out.println("Error: One of the required fields is missing.");
                } else {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除Sponsor记录
    public static void deleteSponsor(int sponsorID) {
        String sql = "DELETE FROM Sponsor WHERE SponsorID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sponsorID);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Sponsor deleted successfully.");
            } else {
                System.out.println("Error: Sponsor with SponsorID " + sponsorID + " not found.");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key constraint")) {
                System.out.println("Error: Cannot delete sponsor as it has active team sponsorships.");
            } else {
                e.printStackTrace();
            }
        }
    }

    // 连接查询：查询所有赞助商及其赞助的球队信息
    public static void fetchSponsorDetails() {
        String sql = "SELECT s.SponsorID, s.Name, s.Industry, s.ContactPerson, " +
                    "ts.TeamID, t.TeamName, ts.Amount, ts.StartDate, ts.EndDate " +
                    "FROM Sponsor s " +
                    "LEFT JOIN TeamSponsor ts ON s.SponsorID = ts.SponsorID " +
                    "LEFT JOIN Team t ON ts.TeamID = t.TeamID";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Sponsor Details");
            System.out.println("---------------");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Industry: %s | Contact: %s | " +
                                "Sponsored Team: %s | Amount: %.2f | Period: %s to %s%n",
                    rs.getInt("SponsorID"),
                    rs.getString("Name"),
                    rs.getString("Industry"),
                    rs.getString("ContactPerson"),
                    rs.getString("TeamName") != null ? rs.getString("TeamName") : "No team",
                    rs.getObject("Amount") != null ? rs.getDouble("Amount") : 0.0,
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 嵌套查询：查询指定行业的所有赞助商
    public static void fetchSponsorsByIndustry(String industry) {
        String sql = "SELECT s.*, " +
                    "(SELECT COUNT(*) FROM TeamSponsor ts WHERE ts.SponsorID = s.SponsorID) as TeamCount " +
                    "FROM Sponsor s " +
                    "WHERE s.Industry = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, industry);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Sponsors in " + industry);
            System.out.println("-------------------------");
            while (rs.next()) {
                System.out.printf("Name: %s | Contact: %s | Teams Sponsored: %d%n",
                    rs.getString("Name"),
                    rs.getString("ContactPerson"),
                    rs.getInt("TeamCount"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 分组查询：按照行业分组统计赞助商
    public static void groupSponsorsByIndustry() {
        String sql = "SELECT Industry, COUNT(*) as SponsorCount, " +
                    "COUNT(DISTINCT ts.TeamID) as TeamsSponsored, " +
                    "SUM(ts.Amount) as TotalSponsorship " +
                    "FROM Sponsor s " +
                    "LEFT JOIN TeamSponsor ts ON s.SponsorID = ts.SponsorID " +
                    "GROUP BY Industry";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Sponsor Statistics by Industry");
            System.out.println("-----------------------------");
            while (rs.next()) {
                System.out.printf("Industry: %s | Sponsors: %d | Teams Sponsored: %d | Total Amount: %.2f%n",
                    rs.getString("Industry"),
                    rs.getInt("SponsorCount"),
                    rs.getInt("TeamsSponsored"),
                    rs.getDouble("TotalSponsorship"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 
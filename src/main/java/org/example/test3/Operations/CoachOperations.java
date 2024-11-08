package org.example.test3.Operations;

import org.example.test3.utils.DatabaseConnection;

import java.sql.*;

public class CoachOperations {

    public static void main(String[] args) {
        // 插入操作
//        insertCoach(1, "Xavi Hernandez", 44, "Spain", "UEFA Pro", 1);
//        insertCoach(2, "Carlo Ancelotti", 65, "Italy", "UEFA Pro", 2);
//        insertCoach(3, "Erik ten Hag", 54, "Netherlands", "UEFA Pro", 3);

        // 删除操作
//        deleteCoach(3);  // 删除ID为3的教练

        // 连接查询操作
        fetchCoachTeamInfo();

        // 嵌套查询操作
        fetchCoachesWithLicense("UEFA Pro");

        // 分组查询操作
        groupCoachesByNationality();
    }

    // 插入Coach记录
    public static void insertCoach(int coachID, String name, int age, String nationality, String licenseLevel, int teamID) {
        String sql = "INSERT INTO Coach (CoachID, Name, Age, Nationality, LicenseLevel, TeamID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, coachID);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, nationality);
            pstmt.setString(5, licenseLevel);
            pstmt.setInt(6, teamID);

            try {
                pstmt.executeUpdate();
                System.out.println("Coach inserted successfully.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) { // Duplicate entry error code for MySQL
                    System.out.println("Error: Duplicate CoachID or TeamID.");
                } else if (e.getErrorCode() == 1048) { // Column cannot be null error code
                    System.out.println("Error: One of the required fields is missing.");
                } else if (e.getMessage().contains("foreign key constraint")) {
                    System.out.println("Error: Invalid TeamID, foreign key constraint failed.");
                } else {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除Coach记录
    public static void deleteCoach(int coachID) {
        String sql = "DELETE FROM Coach WHERE CoachID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, coachID);
            
            try {
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Coach deleted successfully.");
                } else {
                    System.out.println("Error: Coach with CoachID " + coachID + " not found.");
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("foreign key constraint")) {
                    System.out.println("Error: Cannot delete coach as they are referenced by other records.");
                } else {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 连接查询：查询所有教练及其所属球队
    public static void fetchCoachTeamInfo() {
        String sql = "SELECT Coach.Name, Team.TeamName " +
                "FROM Coach " +
                "JOIN Team ON Coach.TeamID = Team.TeamID";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Coach Name | Team Name");
            System.out.println("----------------------");
            while (rs.next()) {
                System.out.println(rs.getString("Name") + " | " + rs.getString("TeamName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 嵌套查询：查询带有指定执教资格的教练以及球队信息
    public static void fetchCoachesWithLicense(String licenseLevel) {
        String sql = "SELECT Name, (SELECT TeamName FROM Team WHERE Team.TeamID = Coach.TeamID) as TeamName " +
                "FROM Coach " +
                "WHERE LicenseLevel = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, licenseLevel);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Coaches with License: " + licenseLevel);
            System.out.println("------------------------------");
            while (rs.next()) {
                System.out.println(rs.getString("Name") + " | " + rs.getString("TeamName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 分组查询：按照教练的国籍分组，统计每个国家的教练数量
    public static void groupCoachesByNationality() {
        String sql = "SELECT Nationality, COUNT(*) AS CoachCount " +
                "FROM Coach " +
                "GROUP BY Nationality";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Nationality | Coach Count");
            System.out.println("--------------------------");
            while (rs.next()) {
                System.out.println(rs.getString("Nationality") + " | " + rs.getInt("CoachCount"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

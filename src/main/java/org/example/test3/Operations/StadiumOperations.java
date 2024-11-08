package org.example.test3.Operations;

import org.example.test3.utils.DatabaseConnection;

import java.sql.*;

public class StadiumOperations {

    public static void main(String[] args) {
        // 插入操作示例
        // insertStadium(4, "Allianz Arena", 75000, "Munich, Germany", 2005);

        // 删除操作示例
        // deleteStadium(4);

        // 连接查询操作
        fetchStadiumDetails();

        // 嵌套查询操作
        fetchStadiumsByCapacity(80000);

        // 分组查询操作
        groupStadiumsByLocation();
    }

    // 插入Stadium记录
    public static void insertStadium(int stadiumID, String name, int capacity, String location, int buildYear) {
        String sql = "INSERT INTO Stadium (StadiumID, Name, Capacity, Location, BuildYear) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stadiumID);
            pstmt.setString(2, name);
            pstmt.setInt(3, capacity);
            pstmt.setString(4, location);
            pstmt.setInt(5, buildYear);

            try {
                pstmt.executeUpdate();
                System.out.println("Stadium inserted successfully.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    System.out.println("Error: Duplicate StadiumID or Name.");
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

    // 删除Stadium记录
    public static void deleteStadium(int stadiumID) {
        String sql = "DELETE FROM Stadium WHERE StadiumID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stadiumID);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Stadium deleted successfully.");
            } else {
                System.out.println("Error: Stadium with StadiumID " + stadiumID + " not found.");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key constraint")) {
                System.out.println("Error: Cannot delete stadium as it is currently used by teams or matches.");
            } else {
                e.printStackTrace();
            }
        }
    }

    // 连接查询：查询所有球场及其主场球队信息
    public static void fetchStadiumDetails() {
        String sql = "SELECT s.StadiumID, s.Name, s.Capacity, s.Location, s.BuildYear, " +
                    "t.TeamID, t.TeamName, t.FoundYear " +
                    "FROM Stadium s " +
                    "LEFT JOIN Team t ON s.StadiumID = t.HomeStadiumID";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Stadium Details");
            System.out.println("---------------");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Capacity: %d | Location: %s | Built: %d | " +
                                "Home Team: %s | Team Founded: %d%n",
                    rs.getInt("StadiumID"),
                    rs.getString("Name"),
                    rs.getInt("Capacity"),
                    rs.getString("Location"),
                    rs.getInt("BuildYear"),
                    rs.getString("TeamName") != null ? rs.getString("TeamName") : "No team",
                    rs.getObject("FoundYear") != null ? rs.getInt("FoundYear") : 0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 嵌套查询：查询容量大于指定值的球场
    public static void fetchStadiumsByCapacity(int minCapacity) {
        String sql = "SELECT s.*, " +
                    "(SELECT COUNT(*) FROM FootballMatch fm WHERE fm.StadiumID = s.StadiumID) as MatchCount " +
                    "FROM Stadium s " +
                    "WHERE s.Capacity >= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, minCapacity);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Stadiums with capacity >= " + minCapacity);
            System.out.println("--------------------------------");
            while (rs.next()) {
                System.out.printf("Name: %s | Capacity: %d | Location: %s | Matches Scheduled: %d%n",
                    rs.getString("Name"),
                    rs.getInt("Capacity"),
                    rs.getString("Location"),
                    rs.getInt("MatchCount"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 分组查询：按照地区分组统计球场
    public static void groupStadiumsByLocation() {
        String sql = "SELECT " +
                    "SUBSTRING_INDEX(Location, ',', -1) as Country, " +
                    "COUNT(*) as StadiumCount, " +
                    "AVG(Capacity) as AvgCapacity, " +
                    "MAX(Capacity) as MaxCapacity " +
                    "FROM Stadium " +
                    "GROUP BY SUBSTRING_INDEX(Location, ',', -1)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Stadium Statistics by Country");
            System.out.println("---------------------------");
            while (rs.next()) {
                System.out.printf("Country: %s | Stadiums: %d | Avg Capacity: %.0f | Max Capacity: %d%n",
                    rs.getString("Country").trim(),
                    rs.getInt("StadiumCount"),
                    rs.getDouble("AvgCapacity"),
                    rs.getInt("MaxCapacity"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 
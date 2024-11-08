package org.example.test3.Operations;

import org.example.test3.utils.DatabaseConnection;

import java.sql.*;

public class PlayerOperations {

    public static void main(String[] args) {
        // 插入操作示例
//        insertPlayer(6, "Kevin De Bruyne", 32, "Midfielder", "Belgium", 1);
//        insertPlayer(7, "Erling Haaland", 23, "Forward", "Norway", 2);
//        insertPlayer(8, "Bruno Fernandes", 29, "Midfielder", "Portugal", 3);

        // 删除操作示例
//        deletePlayer(8);  // 删除ID为8的球员

        // 连接查询操作
        fetchPlayerTeamInfo();

        // 嵌套查询操作
        fetchPlayersByPosition("Forward");

        // 分组查询操作
        groupPlayersByNationality();
    }

    // 插入Player记录
    public static void insertPlayer(int playerID, String name, int age, String position, 
                                  String nationality, int teamID) {
        String sql = "INSERT INTO Player (PlayerID, Name, Age, Position, Nationality, TeamID) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerID);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, position);
            pstmt.setString(5, nationality);
            pstmt.setInt(6, teamID);

            try {
                pstmt.executeUpdate();
                System.out.println("Player inserted successfully.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    System.out.println("Error: Duplicate PlayerID.");
                } else if (e.getErrorCode() == 1048) {
                    System.out.println("Error: One of the required fields is missing.");
                } else if (e.getMessage().contains("foreign key constraint")) {
                    System.out.println("Error: Invalid TeamID, foreign key constraint failed.");
                } else if (e.getMessage().contains("CHECK constraint")) {
                    System.out.println("Error: Age must be between 16 and 45.");
                } else {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除Player记录
    public static void deletePlayer(int playerID) {
        String sql = "DELETE FROM Player WHERE PlayerID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerID);
            
            try {
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Player deleted successfully.");
                } else {
                    System.out.println("Error: Player with PlayerID " + playerID + " not found.");
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("foreign key constraint")) {
                    System.out.println("Error: Cannot delete player as they have active contracts.");
                } else {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 连接查询：查询所有球员及其所属球队
    public static void fetchPlayerTeamInfo() {
        String sql = "SELECT Player.Name, Player.Position, Team.TeamName " +
                    "FROM Player " +
                    "JOIN Team ON Player.TeamID = Team.TeamID";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Player Name | Position | Team Name");
            System.out.println("--------------------------------");
            while (rs.next()) {
                System.out.println(rs.getString("Name") + " | " + 
                                 rs.getString("Position") + " | " + 
                                 rs.getString("TeamName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 嵌套查询：查询指定位置的球员及其球队信息
    public static void fetchPlayersByPosition(String position) {
        String sql = "SELECT Name, Age, (SELECT TeamName FROM Team WHERE Team.TeamID = Player.TeamID) as TeamName " +
                    "FROM Player " +
                    "WHERE Position = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, position);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Players in position: " + position);
            System.out.println("--------------------------------");
            while (rs.next()) {
                System.out.println(rs.getString("Name") + " | Age: " + 
                                 rs.getInt("Age") + " | Team: " + 
                                 rs.getString("TeamName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 分组查询：按照球员的国籍分组，统计每个国家的球员数量和平均年龄
    public static void groupPlayersByNationality() {
        String sql = "SELECT Nationality, COUNT(*) AS PlayerCount, " +
                    "AVG(Age) AS AverageAge " +
                    "FROM Player " +
                    "GROUP BY Nationality";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Nationality | Player Count | Average Age");
            System.out.println("----------------------------------------");
            while (rs.next()) {
                System.out.printf("%s | %d | %.1f%n", 
                    rs.getString("Nationality"),
                    rs.getInt("PlayerCount"),
                    rs.getDouble("AverageAge"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 
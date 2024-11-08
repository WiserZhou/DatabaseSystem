package org.example.test3.Operations;

import org.example.test3.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

public class ContractOperations {

    public static void main(String[] args) {
        // 插入操作示例
        // insertContract(4, 3, 3, LocalDate.of(2024, 1, 1), 
        //               LocalDate.of(2026, 12, 31), 45000000.00, "Active");

        // 删除操作示例
        // deleteContract(4);

        // 连接查询操作
        fetchContractPlayerInfo();

        // 嵌套查询操作
        fetchContractsByTeam("FC Barcelona");

        // 分组查询操作
        groupContractsByStatus();
    }

    // 插入Contract记录
    public static void insertContract(int contractID, int playerID, int teamID, 
                                    LocalDate startDate, LocalDate endDate, 
                                    double value, String status) {
        String sql = "INSERT INTO Contract (ContractID, PlayerID, TeamID, StartDate, EndDate, Value, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, contractID);
            pstmt.setInt(2, playerID);
            pstmt.setInt(3, teamID);
            pstmt.setDate(4, Date.valueOf(startDate));
            pstmt.setDate(5, Date.valueOf(endDate));
            pstmt.setDouble(6, value);
            pstmt.setString(7, status);

            try {
                pstmt.executeUpdate();
                System.out.println("Contract inserted successfully.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    System.out.println("Error: Duplicate ContractID.");
                } else if (e.getErrorCode() == 1048) {
                    System.out.println("Error: One of the required fields is missing.");
                } else if (e.getMessage().contains("foreign key constraint")) {
                    System.out.println("Error: Invalid PlayerID or TeamID, foreign key constraint failed.");
                } else if (e.getMessage().contains("CHECK constraint")) {
                    System.out.println("Error: StartDate must be before EndDate.");
                } else {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除Contract记录
    public static void deleteContract(int contractID) {
        String sql = "DELETE FROM Contract WHERE ContractID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, contractID);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Contract deleted successfully.");
            } else {
                System.out.println("Error: Contract with ContractID " + contractID + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 连接查询：查询所有合同及相关球员和球队信息
    public static void fetchContractPlayerInfo() {
        String sql = "SELECT Contract.ContractID, Player.Name as PlayerName, " +
                    "Team.TeamName, Contract.StartDate, Contract.EndDate, " +
                    "Contract.Value, Contract.Status " +
                    "FROM Contract " +
                    "JOIN Player ON Contract.PlayerID = Player.PlayerID " +
                    "JOIN Team ON Contract.TeamID = Team.TeamID";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Contract Details");
            System.out.println("----------------");
            while (rs.next()) {
                System.out.printf("ID: %d | Player: %s | Team: %s | Period: %s to %s | Value: %.2f | Status: %s%n",
                    rs.getInt("ContractID"),
                    rs.getString("PlayerName"),
                    rs.getString("TeamName"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getDouble("Value"),
                    rs.getString("Status"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 嵌套查询：查询指定球队的所有合同
    public static void fetchContractsByTeam(String teamName) {
        String sql = "SELECT Contract.ContractID, Player.Name, Contract.Value, Contract.Status " +
                    "FROM Contract " +
                    "JOIN Player ON Contract.PlayerID = Player.PlayerID " +
                    "WHERE Contract.TeamID = (SELECT TeamID FROM Team WHERE TeamName = ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teamName);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Contracts for team: " + teamName);
            System.out.println("--------------------------------");
            while (rs.next()) {
                System.out.printf("Contract ID: %d | Player: %s | Value: %.2f | Status: %s%n",
                    rs.getInt("ContractID"),
                    rs.getString("Name"),
                    rs.getDouble("Value"),
                    rs.getString("Status"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 分组查询：按照合同状态分组，统计每种状态的合同数量和平均价值
    public static void groupContractsByStatus() {
        String sql = "SELECT Status, COUNT(*) AS ContractCount, " +
                    "AVG(Value) AS AverageValue " +
                    "FROM Contract " +
                    "GROUP BY Status";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Contract Statistics by Status");
            System.out.println("----------------------------");
            while (rs.next()) {
                System.out.printf("Status: %s | Count: %d | Average Value: %.2f%n",
                    rs.getString("Status"),
                    rs.getInt("ContractCount"),
                    rs.getDouble("AverageValue"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 
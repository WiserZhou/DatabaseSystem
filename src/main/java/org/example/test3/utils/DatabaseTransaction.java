package org.example.test3.utils;


import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTransaction {
    private static Connection connection;
    
    public static void beginTransaction() throws SQLException {
        connection = DatabaseConnection.getConnection();
        connection.setAutoCommit(false);
    }
    
    public static void commitTransaction() throws SQLException {
        try {
            connection.commit();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }
    
    public static void rollbackTransaction() {
        try {
            if (connection != null) {
                connection.rollback();
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 
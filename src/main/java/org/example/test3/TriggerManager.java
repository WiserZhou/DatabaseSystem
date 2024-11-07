import org.example.test3.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TriggerManager {
//    public static void enableTrigger(String triggerName) throws SQLException {
//        String sql = "ALTER TRIGGER " + triggerName + " ENABLE";
//        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
//            stmt.execute(sql);
//        }
//    }
//
//    public static void disableTrigger(String triggerName) throws SQLException {
//        String sql = "ALTER TRIGGER " + triggerName + " DISABLE";
//        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
//            stmt.execute(sql);
//        }
//    }
//
//    public static List<String> listTriggers() throws SQLException {
//        List<String> triggers = new ArrayList<>();
//        String sql = "SELECT trigger_name FROM user_triggers";
//        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                triggers.add(rs.getString("trigger_name"));
//            }
//        }
//        return triggers;
//    }
} 
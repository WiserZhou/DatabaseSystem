package org.example.test3;

import javax.swing.*;
import java.awt.*;

public class Main {
//    private static UserDAO userDAO;
//    private static FriendDAO friendDAO;
//    private static LogDAO logDAO;

    public static void main(String[] args) {
//        userDAO = new UserDAO();
//        friendDAO = new FriendDAO();
//        logDAO = new LogDAO();
//
//        createAndShowGUI();
    }

//    private static void createAndShowGUI() {
//        JFrame frame = new JFrame("用户管理系统");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(600, 400);
//
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
//
//        // 用户操作面板
//        JPanel userPanel = createUserPanel();
//        // 好友操作面板
//        JPanel friendPanel = createFriendPanel();
//        // 日志操作面板
//        JPanel logPanel = createLogPanel();
//
//        mainPanel.add(userPanel);
//        mainPanel.add(friendPanel);
//        mainPanel.add(logPanel);
//
//        frame.add(mainPanel);
//        frame.setVisible(true);
//    }
//
//    private static JPanel createUserPanel() {
//        JPanel panel = new JPanel();
//        panel.setBorder(BorderFactory.createTitledBorder("用户操作"));
//
//        JTextField nameField = new JTextField(15);
//        JTextField emailField = new JTextField(15);
//        JPasswordField passwordField = new JPasswordField(15);
//
//        JButton addButton = new JButton("添加用户");
//        addButton.addActionListener(e -> {
//            String name = nameField.getText();
//            String email = emailField.getText();
//            String password = new String(passwordField.getPassword());
//            userDAO.insertUser(name, email, password);
//            JOptionPane.showMessageDialog(null, "用户添加成功！");
//        });
//
//        panel.add(new JLabel("姓名:"));
//        panel.add(nameField);
//        panel.add(new JLabel("邮箱:"));
//        panel.add(emailField);
//        panel.add(new JLabel("密码:"));
//        panel.add(passwordField);
//        panel.add(addButton);
//
//        return panel;
//    }
//
//    private static JPanel createFriendPanel() {
//        JPanel panel = new JPanel();
//        panel.setBorder(BorderFactory.createTitledBorder("好友操作"));
//
//        JTextField user1Field = new JTextField(10);
//        JTextField user2Field = new JTextField(10);
//
//        JButton addButton = new JButton("添加好友");
//        addButton.addActionListener(e -> {
//            try {
//                int user1Id = Integer.parseInt(user1Field.getText());
//                int user2Id = Integer.parseInt(user2Field.getText());
//                friendDAO.addFriend(user1Id, user2Id);
//                JOptionPane.showMessageDialog(null, "好友添加成功！");
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(null, "请输入有效的用户ID！");
//            }
//        });
//
//        panel.add(new JLabel("用户1 ID:"));
//        panel.add(user1Field);
//        panel.add(new JLabel("用户2 ID:"));
//        panel.add(user2Field);
//        panel.add(addButton);
//
//        return panel;
//    }
//
//    private static JPanel createLogPanel() {
//        JPanel panel = new JPanel();
//        panel.setBorder(BorderFactory.createTitledBorder("日志操作"));
//
//        JTextField userIdField = new JTextField(10);
//        JTextField contentField = new JTextField(20);
//
//        JButton addButton = new JButton("添加日志");
//        JButton deleteButton = new JButton("删除日志");
//
//        addButton.addActionListener(e -> {
//            try {
//                int userId = Integer.parseInt(userIdField.getText());
//                String content = contentField.getText();
//                logDAO.addLog(userId, content);
//                JOptionPane.showMessageDialog(null, "日志添加成功！");
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(null, "请输入有效的用户ID！");
//            }
//        });
//
//        deleteButton.addActionListener(e -> {
//            try {
//                int logId = Integer.parseInt(contentField.getText());
//                logDAO.deleteLog(logId);
//                JOptionPane.showMessageDialog(null, "日志删除成功！");
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(null, "请输入有效的日志ID！");
//            }
//        });
//
//        panel.add(new JLabel("用户ID:"));
//        panel.add(userIdField);
//        panel.add(new JLabel("内容:"));
//        panel.add(contentField);
//        panel.add(addButton);
//        panel.add(deleteButton);
//
//        return panel;
//    }
}

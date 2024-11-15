package org.example.test3;

import org.example.test3.Operations.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.PrintStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

public class FootballManagementUI {
    private JFrame frame;
    private JTextArea outputArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FootballManagementUI::new);
    }

    public FootballManagementUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Football Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        // 设置整体背景颜色
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        // 创建选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14)); // 设置选项卡字体
        tabbedPane.setBackground(new Color(230, 230, 250)); // 设置选项卡背景颜色

        // 添加输出区域
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(new Color(245, 245, 245)); // 背景颜色
        outputArea.setForeground(new Color(50, 50, 50));    // 字体颜色

        // 创建滚动面板并设置
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputScrollPane.setPreferredSize(new Dimension(1200, 300));
        outputScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204)), "Output",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14), new Color(0, 102, 204)));

        // 为每个选项卡添加不同的面板
        tabbedPane.addTab("Team", createStyledPanel(createTeamPanel()));
        tabbedPane.addTab("Player", createStyledPanel(createPlayerPanel()));
        tabbedPane.addTab("Coach", createStyledPanel(createCoachPanel()));
        tabbedPane.addTab("Stadium", createStyledPanel(createStadiumPanel()));
        tabbedPane.addTab("Match", createStyledPanel(createMatchPanel()));
        tabbedPane.addTab("Sponsor", createStyledPanel(createSponsorPanel()));
        tabbedPane.addTab("Contract", createStyledPanel(createContractPanel()));
        tabbedPane.addTab("Team Sponsor", createStyledPanel(createTeamSponsorPanel()));

        // 添加组件到框架
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(outputScrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // 创建一个带样式的面板（用于主内容）
    private JPanel createStyledPanel(JPanel panel) {
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), // 外层灰色边框，带圆角
                BorderFactory.createEmptyBorder(15, 15, 15, 15) // 内层空白边距
        ));
        panel.setBackground(new Color(245, 245, 250)); // 浅灰蓝色背景
        return panel;
    }

    // 创建基础面板（用于布局整体结构）
    private JPanel createBasePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true), // 边框带圆角
                BorderFactory.createEmptyBorder(15, 15, 15, 15) // 内边距
        ));
        panel.setBackground(new Color(250, 250, 255)); // 白色背景带一点淡蓝
        return panel;
    }

    // 创建按钮面板
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 按钮区域增加上下内边距
        buttonPanel.setBackground(new Color(235, 240, 255)); // 按钮面板浅蓝色背景
        return buttonPanel;
    }

    // 美化按钮的方法
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13)); // 设置粗体字体
        button.setForeground(Color.WHITE); // 字体颜色
        button.setBackground(new Color(60, 130, 200)); // 按钮蓝色背景
        button.setFocusPainted(false); // 去除聚焦边框
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 120, 190), 1), // 深蓝色边框
                BorderFactory.createEmptyBorder(8, 15, 8, 15) // 内边距
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 鼠标悬停手形指针
        return button;
    }


    private JPanel createTeamPanel() {
        JPanel mainPanel = createBasePanel();

        // 创建顶部按钮面板
        JPanel buttonPanel = createButtonPanel();

        // 添加“View All Teams”按钮
        JButton viewButton = createStyledButton("View All Teams");
        viewButton.addActionListener(e -> {
            outputArea.append("\n----- View All Teams -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, TeamOperations::fetchTeamDetails);
        });

        // 添加“Group Teams by Stadium Location”按钮
        JButton groupButton = createStyledButton("Group Teams by Stadium Location");
        groupButton.addActionListener(e -> {
            outputArea.append("\n----- Group Teams by Stadium Location -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, TeamOperations::groupTeamsByStadiumLocation);
        });

        // 嵌套查询面板，使用FlowLayout紧凑布局
        JPanel nestedQueryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nestedQueryPanel.add(new JLabel("Found Year:"));
        JTextField yearQueryField = new JTextField(6);
        // 创建“Search”按钮，并添加事件监听器
        JButton nestedQueryButton = createStyledButton("Search");
        nestedQueryButton.addActionListener(e -> {
            String yearText = yearQueryField.getText().trim();

            // 检查年份字段是否为空
            if (yearText.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter a year to search",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int year = Integer.parseInt(yearText);

                // 验证年份的合理范围
                if (year < 1800 || year > 2024) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid year (1800-2024)",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 执行嵌套查询操作，并输出结果到 outputArea
                outputArea.append("\n----- Searching Teams Founded in " + year + " -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> TeamOperations.fetchTeamsByFoundYear(year));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter a valid year (numeric value)",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });


        nestedQueryPanel.add(yearQueryField);
        nestedQueryPanel.add(nestedQueryButton);
        nestedQueryPanel.setBackground(new Color(240, 240, 250));

        // 将按钮添加到按钮面板
        buttonPanel.add(viewButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(nestedQueryPanel);

        // 创建中间的操作面板，使用紧凑的网格布局
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));

        // 添加表单面板
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 1, true),
                "Add Team", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14), new Color(70, 130, 180)));
        addPanel.setBackground(new Color(245, 245, 255));

        // 表单布局配置
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 添加标签和输入框
        addFieldToPanel(addPanel, gbc, "Team ID:", 0);
        JTextField idField = new JTextField(15);
        addInputField(addPanel, gbc, idField, 0);

        addFieldToPanel(addPanel, gbc, "Team Name:", 1);
        JTextField nameField = new JTextField(15);
        addInputField(addPanel, gbc, nameField, 1);

        addFieldToPanel(addPanel, gbc, "Found Year:", 2);
        JTextField yearField = new JTextField(15);
        addInputField(addPanel, gbc, yearField, 2);

        addFieldToPanel(addPanel, gbc, "Stadium ID:", 3);
        JTextField stadiumField = new JTextField(15);
        addInputField(addPanel, gbc, stadiumField, 3);

        // 添加"Add Team"按钮
        JButton addButton = createStyledButton("Add Team");
        addButton.addActionListener(e -> handleAddButton(idField, nameField, yearField, stadiumField));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addButton, gbc);

        // 删除面板
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 99, 71), 1, true),
                "Delete Team", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14), new Color(220, 20, 60)));
        deletePanel.setBackground(new Color(255, 240, 245));

        // 删除字段和按钮
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        deletePanel.add(new JLabel("Team ID to delete:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField deleteIdField = new JTextField(15);
        deletePanel.add(deleteIdField, gbc);

        JButton deleteButton = createStyledButton("Delete Team");
        deleteButton.addActionListener(e -> handleDeleteButton(deleteIdField));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deletePanel.add(deleteButton, gbc);

        // 将表单面板和删除面板添加到操作面板
        operationsPanel.add(addPanel);
        operationsPanel.add(deletePanel);

        // 将按钮面板和操作面板添加到主面板
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(operationsPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    // 添加标签到指定面板
    private void addFieldToPanel(JPanel panel, GridBagConstraints gbc, String label, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
    }

    // 添加输入框到指定面板
    private void addInputField(JPanel panel, GridBagConstraints gbc, JTextField field, int row) {
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    // 处理添加按钮点击事件
    private void handleAddButton(JTextField idField, JTextField nameField, JTextField yearField, JTextField stadiumField) {
        // 首先验证所有字段是否都已填写
        if (idField.getText().trim().isEmpty() ||
                nameField.getText().trim().isEmpty() ||
                yearField.getText().trim().isEmpty() ||
                stadiumField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Please fill in all fields",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            int stadiumId = Integer.parseInt(stadiumField.getText().trim());

            // 添加基本的数据验证
            if (year < 1800 || year > 2024) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter a valid year (1800-2024)",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            outputArea.append("\n----- Adding New Team -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, () -> TeamOperations.insertTeam(id, name, year, stadiumId));

            // 清空输入框
            idField.setText("");
            nameField.setText("");
            yearField.setText("");
            stadiumField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter valid numbers for ID, Year and Stadium ID",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // 处理删除按钮点击事件
    private void handleDeleteButton(JTextField deleteIdField) {
        // 验证删除ID是否已填写
        if (deleteIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter a Team ID to delete",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(deleteIdField.getText().trim());
            if (id <= 0) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter a valid Team ID (positive number)",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete team with ID " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                outputArea.append("\n----- Deleting Team -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> TeamOperations.deleteTeam(id));
                deleteIdField.setText(""); // 清空输入框
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter a valid Team ID (number only)",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // 其他面板的创建方法类似，这里只展示部分代码
    private JPanel createPlayerPanel() {
        JPanel mainPanel = createBasePanel();
        
        // 创建顶部按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加查询按钮和嵌套查询
        JButton viewButton = new JButton("View All Players");
        viewButton.addActionListener(e -> {
            outputArea.append("\n----- View All Players -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, PlayerOperations::fetchPlayerTeamInfo);
        });

        JButton groupButton = new JButton("Group Players by Nationality");
        groupButton.addActionListener(e -> {
            outputArea.append("\n----- Group Players by Nationality -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, PlayerOperations::groupPlayersByNationality);
        });
        
        // 嵌套查询面板
        JPanel nestedQueryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nestedQueryPanel.add(new JLabel("Position:"));
        JTextField positionQueryField = new JTextField(15);
        JButton nestedQueryButton = getjButton7(positionQueryField);

        nestedQueryPanel.add(positionQueryField);
        nestedQueryPanel.add(nestedQueryButton);

        buttonPanel.add(viewButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(nestedQueryPanel);

        // 创建中间的操作面板
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // 添加表单面板
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Player"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 标签列
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        addPanel.add(new JLabel("Player ID:"), gbc);
        
        gbc.gridy = 1;
        addPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridy = 2;
        addPanel.add(new JLabel("Age:"), gbc);
        
        gbc.gridy = 3;
        addPanel.add(new JLabel("Position:"), gbc);
        
        gbc.gridy = 4;
        addPanel.add(new JLabel("Nationality:"), gbc);
        
        gbc.gridy = 5;
        addPanel.add(new JLabel("Team ID:"), gbc);
        
        // 输入框列
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField idField = new JTextField(15);
        addPanel.add(idField, gbc);
        
        gbc.gridy = 1;
        JTextField nameField = new JTextField(15);
        addPanel.add(nameField, gbc);
        
        gbc.gridy = 2;
        JTextField ageField = new JTextField(15);
        addPanel.add(ageField, gbc);
        
        gbc.gridy = 3;
        JTextField positionField = new JTextField(15);
        addPanel.add(positionField, gbc);
        
        gbc.gridy = 4;
        JTextField nationalityField = new JTextField(15);
        addPanel.add(nationalityField, gbc);
        
        gbc.gridy = 5;
        JTextField teamIdField = new JTextField(15);
        addPanel.add(teamIdField, gbc);
        
        // 添加按钮
        JButton addButton = new JButton("Add Player");
        addButton.addActionListener(e -> {
            // 验证所有字段是否填写
            if (idField.getText().trim().isEmpty() || 
                nameField.getText().trim().isEmpty() || 
                ageField.getText().trim().isEmpty() || 
                positionField.getText().trim().isEmpty() || 
                nationalityField.getText().trim().isEmpty() || 
                teamIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please fill in all fields", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int playerId = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String position = positionField.getText().trim();
                String nationality = nationalityField.getText().trim();
                int teamId = Integer.parseInt(teamIdField.getText().trim());
                
                // 验证年龄
                if (age < 16 || age > 45) {
                    JOptionPane.showMessageDialog(frame, 
                        "Please enter a valid age (16-45)", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                outputArea.append("\n----- Adding New Player -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> PlayerOperations.insertPlayer(playerId, name, age, position, nationality, teamId));
                
                // 清空输入框
                idField.setText("");
                nameField.setText("");
                ageField.setText("");
                positionField.setText("");
                nationalityField.setText("");
                teamIdField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter valid numbers for ID, Age and Team ID", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addButton, gbc);

        // 删除面板
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Player"));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        deletePanel.add(new JLabel("Player ID to delete:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField deleteIdField = new JTextField(15);
        deletePanel.add(deleteIdField, gbc);

        JButton deleteButton = getjButton8(deleteIdField);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deletePanel.add(deleteButton, gbc);

        // 添加到操作面板
        operationsPanel.add(addPanel);
        operationsPanel.add(deletePanel);
        
        // 添加到主面板
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(operationsPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JButton getjButton8(JTextField deleteIdField) {
        JButton deleteButton = new JButton("Delete Player");
        deleteButton.addActionListener(e -> {
            if (deleteIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a Player ID to delete",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(deleteIdField.getText().trim());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(frame,
                        "Please enter a valid Player ID (positive number)",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete player with ID " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    outputArea.append("\n----- Deleting Player -----\n");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> PlayerOperations.deletePlayer(id));
                    deleteIdField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a valid Player ID (number only)",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        return deleteButton;
    }

    private JButton getjButton7(JTextField positionQueryField) {
        JButton nestedQueryButton = new JButton("Find Players by Position");
        nestedQueryButton.addActionListener(e -> {
            String position = positionQueryField.getText().trim();
            if (!position.isEmpty()) {
                outputArea.append("\n----- Players in Position: " + position + " -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> PlayerOperations.fetchPlayersByPosition(position));
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a position");
            }
        });
        return nestedQueryButton;
    }

    private JPanel createCoachPanel() {
        JPanel mainPanel = createBasePanel();
        
        // 创建顶部按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加查询按钮和嵌套查询
        JButton viewButton = new JButton("View All Coaches");
        viewButton.addActionListener(e -> {
            outputArea.append("\n----- View All Coaches -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, CoachOperations::fetchCoachTeamInfo);
        });

        JButton groupButton = new JButton("Group Coaches by Nationality");
        groupButton.addActionListener(e -> {
            outputArea.append("\n----- Group Coaches by Nationality -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, CoachOperations::groupCoachesByNationality);
        });
        
        // 嵌套查询面板
        JPanel nestedQueryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nestedQueryPanel.add(new JLabel("License Level:"));
        JTextField licenseQueryField = new JTextField(10);
        JButton nestedQueryButton = getjButton(licenseQueryField);

        nestedQueryPanel.add(licenseQueryField);
        nestedQueryPanel.add(nestedQueryButton);

        buttonPanel.add(viewButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(nestedQueryPanel);

        // 创建中间的操作面板
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // 添加表单面板
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Coach"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 标签列
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        addPanel.add(new JLabel("Coach ID:"), gbc);
        
        gbc.gridy = 1;
        addPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridy = 2;
        addPanel.add(new JLabel("Age:"), gbc);
        
        gbc.gridy = 3;
        addPanel.add(new JLabel("Nationality:"), gbc);
        
        gbc.gridy = 4;
        addPanel.add(new JLabel("License Level:"), gbc);
        
        gbc.gridy = 5;
        addPanel.add(new JLabel("Team ID:"), gbc);
        
        // 输入框列
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField idField = new JTextField(15);
        addPanel.add(idField, gbc);
        
        gbc.gridy = 1;
        JTextField nameField = new JTextField(15);
        addPanel.add(nameField, gbc);
        
        gbc.gridy = 2;
        JTextField ageField = new JTextField(15);
        addPanel.add(ageField, gbc);
        
        gbc.gridy = 3;
        JTextField nationalityField = new JTextField(15);
        addPanel.add(nationalityField, gbc);
        
        gbc.gridy = 4;
        JTextField licenseLevelField = new JTextField(15);
        addPanel.add(licenseLevelField, gbc);
        
        gbc.gridy = 5;
        JTextField teamIdField = new JTextField(15);
        addPanel.add(teamIdField, gbc);
        
        // 添加按钮
        JButton addButton = new JButton("Add Coach");
        addButton.addActionListener(e -> {
            if (idField.getText().trim().isEmpty() || 
                nameField.getText().trim().isEmpty() || 
                ageField.getText().trim().isEmpty() || 
                nationalityField.getText().trim().isEmpty() || 
                licenseLevelField.getText().trim().isEmpty() || 
                teamIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please fill in all fields", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String nationality = nationalityField.getText().trim();
                String licenseLevel = licenseLevelField.getText().trim();
                int teamId = Integer.parseInt(teamIdField.getText().trim());
                
                if (age < 18 || age > 100) {
                    JOptionPane.showMessageDialog(frame, 
                        "Please enter a valid age (18-100)", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                outputArea.append("\n----- Adding New Coach -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> CoachOperations.insertCoach(id, name, age, nationality, licenseLevel, teamId));
                
                // 清空输入框
                idField.setText("");
                nameField.setText("");
                ageField.setText("");
                nationalityField.setText("");
                licenseLevelField.setText("");
                teamIdField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter valid numbers for ID, Age and Team ID", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addButton, gbc);

        // 删除面板
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Coach"));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        deletePanel.add(new JLabel("Coach ID to delete:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField deleteIdField = new JTextField(15);
        deletePanel.add(deleteIdField, gbc);

        JButton deleteButton = getjButton6(deleteIdField);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deletePanel.add(deleteButton, gbc);

        // 添加到操作面板
        operationsPanel.add(addPanel);
        operationsPanel.add(deletePanel);
        
        // 添加到主面板
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(operationsPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JButton getjButton6(JTextField deleteIdField) {
        JButton deleteButton = new JButton("Delete Coach");
        deleteButton.addActionListener(e -> {
            if (deleteIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a Coach ID to delete",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(deleteIdField.getText().trim());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(frame,
                        "Please enter a valid Coach ID (positive number)",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete coach with ID " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    outputArea.append("\n----- Deleting Coach -----\n");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> CoachOperations.deleteCoach(id));
                    deleteIdField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a valid Coach ID (number only)",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        return deleteButton;
    }

    private JButton getjButton(JTextField licenseQueryField) {
        JButton nestedQueryButton = new JButton("Find Coaches by License");
        nestedQueryButton.addActionListener(e -> {
            String license = licenseQueryField.getText().trim();
            if (!license.isEmpty()) {
                outputArea.append("\n----- Coaches with License: " + license + " -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> CoachOperations.fetchCoachesWithLicense(license));
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a license level");
            }
        });
        return nestedQueryButton;
    }

    private JPanel createStadiumPanel() {
        JPanel mainPanel = createBasePanel();
        
        // 创建顶部按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加查询按钮和嵌套查询
        JButton viewButton = new JButton("View All Stadiums");
        viewButton.addActionListener(e -> {
            outputArea.append("\n----- View All Stadiums -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, StadiumOperations::fetchStadiumDetails);
        });

        JButton groupButton = new JButton("Group Stadiums by Location");
        groupButton.addActionListener(e -> {
            outputArea.append("\n----- Group Stadiums by Location -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, StadiumOperations::groupStadiumsByLocation);
        });
        
        // 嵌套查询面板
        JPanel nestedQueryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nestedQueryPanel.add(new JLabel("Minimum Capacity:"));
        JTextField capacityQueryField = new JTextField(10);
        JButton nestedQueryButton = getNestedQueryButton(capacityQueryField);

        nestedQueryPanel.add(capacityQueryField);
        nestedQueryPanel.add(nestedQueryButton);

        buttonPanel.add(viewButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(nestedQueryPanel);

        // 创建中间的操作面板
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // 添加表单面板
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Stadium"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 标签列
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        addPanel.add(new JLabel("Stadium ID:"), gbc);
        
        gbc.gridy = 1;
        addPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridy = 2;
        addPanel.add(new JLabel("Capacity:"), gbc);
        
        gbc.gridy = 3;
        addPanel.add(new JLabel("Location:"), gbc);
        
        gbc.gridy = 4;
        addPanel.add(new JLabel("Build Year:"), gbc);
        
        // 输入框列
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField idField = new JTextField(15);
        addPanel.add(idField, gbc);
        
        gbc.gridy = 1;
        JTextField nameField = new JTextField(15);
        addPanel.add(nameField, gbc);
        
        gbc.gridy = 2;
        JTextField capacityField = new JTextField(15);
        addPanel.add(capacityField, gbc);
        
        gbc.gridy = 3;
        JTextField locationField = new JTextField(15);
        addPanel.add(locationField, gbc);
        
        gbc.gridy = 4;
        JTextField buildYearField = new JTextField(15);
        addPanel.add(buildYearField, gbc);
        
        // 添加按钮
        JButton addButton = new JButton("Add Stadium");
        addButton.addActionListener(e -> {
            // 验证所有字段是否填写
            if (idField.getText().trim().isEmpty() || 
                nameField.getText().trim().isEmpty() || 
                capacityField.getText().trim().isEmpty() || 
                locationField.getText().trim().isEmpty() || 
                buildYearField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please fill in all fields", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int stadiumId = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                int capacity = Integer.parseInt(capacityField.getText().trim());
                String location = locationField.getText().trim();
                int buildYear = Integer.parseInt(buildYearField.getText().trim());
                
                // 验证容量
                if (capacity <= 0) {
                    JOptionPane.showMessageDialog(frame, 
                        "Capacity must be positive", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 验证建造年份
                int currentYear = java.time.Year.now().getValue();
                if (buildYear < 1800 || buildYear > currentYear) {
                    JOptionPane.showMessageDialog(frame, 
                        "Please enter a valid build year (1800-" + currentYear + ")", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                outputArea.append("\n----- Adding New Stadium -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> StadiumOperations.insertStadium(stadiumId, name, capacity, location, buildYear));
                
                // 清空输入框
                idField.setText("");
                nameField.setText("");
                capacityField.setText("");
                locationField.setText("");
                buildYearField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter valid numbers for ID, Capacity and Build Year", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addButton, gbc);

        // 删除面板
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Stadium"));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        deletePanel.add(new JLabel("Stadium ID to delete:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField deleteIdField = new JTextField(15);
        deletePanel.add(deleteIdField, gbc);

        JButton deleteButton = getDeleteButton(deleteIdField);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deletePanel.add(deleteButton, gbc);

        // 添加到操作面板
        operationsPanel.add(addPanel);
        operationsPanel.add(deletePanel);
        
        // 添加到主面板
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(operationsPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JButton getDeleteButton(JTextField deleteIdField) {
        JButton deleteButton = new JButton("Delete Stadium");
        deleteButton.addActionListener(e -> {
            if (deleteIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a Stadium ID to delete",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(deleteIdField.getText().trim());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(frame,
                        "Please enter a valid Stadium ID (positive number)",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete stadium with ID " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    outputArea.append("\n----- Deleting Stadium -----\n");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> StadiumOperations.deleteStadium(id));
                    deleteIdField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a valid Stadium ID (number only)",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        return deleteButton;
    }

    private JButton getNestedQueryButton(JTextField capacityQueryField) {
        JButton nestedQueryButton = new JButton("Find Stadiums by Capacity");
        nestedQueryButton.addActionListener(e -> {
            try {
                int minCapacity = Integer.parseInt(capacityQueryField.getText().trim());
                if (minCapacity > 0) {
                    outputArea.append("\n----- Stadiums with Capacity >= " + minCapacity + " -----\n");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> StadiumOperations.fetchStadiumsByCapacity(minCapacity));
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a positive capacity");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number");
            }
        });
        return nestedQueryButton;
    }

    private JPanel createMatchPanel() {
        JPanel mainPanel = createBasePanel();
        
        // 创建顶部按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加查询按钮和嵌套查询
        JButton viewButton = new JButton("View All Matches");
        viewButton.addActionListener(e -> {
            outputArea.append("\n----- View All Matches -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, FootballMatchOperations::fetchMatchDetails);
        });

        JButton groupButton = new JButton("Group Matches by Status");
        groupButton.addActionListener(e -> {
            outputArea.append("\n----- Group Matches by Status -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, FootballMatchOperations::groupMatchesByStatus);
        });
        
        // 嵌套查询面板
        JPanel nestedQueryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nestedQueryPanel.add(new JLabel("Stadium Name:"));
        JTextField stadiumQueryField = new JTextField(15);
        JButton nestedQueryButton = getButton(stadiumQueryField);

        nestedQueryPanel.add(stadiumQueryField);
        nestedQueryPanel.add(nestedQueryButton);

        buttonPanel.add(viewButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(nestedQueryPanel);

        // 创建中间的操作面板
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // 添加表单面板
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Match"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 标签列
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        addPanel.add(new JLabel("Match ID:"), gbc);
        
        gbc.gridy = 1;
        addPanel.add(new JLabel("Home Team ID:"), gbc);
        
        gbc.gridy = 2;
        addPanel.add(new JLabel("Away Team ID:"), gbc);
        
        gbc.gridy = 3;
        addPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridy = 4;
        addPanel.add(new JLabel("Time (HH:mm:ss):"), gbc);
        
        gbc.gridy = 5;
        addPanel.add(new JLabel("Stadium ID:"), gbc);
        
        gbc.gridy = 6;
        addPanel.add(new JLabel("Status:"), gbc);
        
        // 输入框列
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField idField = new JTextField(15);
        addPanel.add(idField, gbc);
        
        gbc.gridy = 1;
        JTextField homeTeamField = new JTextField(15);
        addPanel.add(homeTeamField, gbc);
        
        gbc.gridy = 2;
        JTextField awayTeamField = new JTextField(15);
        addPanel.add(awayTeamField, gbc);
        
        gbc.gridy = 3;
        JTextField dateField = new JTextField(15);
        addPanel.add(dateField, gbc);
        
        gbc.gridy = 4;
        JTextField timeField = new JTextField(15);
        addPanel.add(timeField, gbc);
        
        gbc.gridy = 5;
        JTextField stadiumIdField = new JTextField(15);
        addPanel.add(stadiumIdField, gbc);
        
        gbc.gridy = 6;
        JTextField statusField = new JTextField(15);
        addPanel.add(statusField, gbc);
        
        // 添加按钮
        JButton addButton = new JButton("Add Match");
        addButton.addActionListener(e -> {
            // 验证所有字段是否填写
            if (idField.getText().trim().isEmpty() || 
                homeTeamField.getText().trim().isEmpty() || 
                awayTeamField.getText().trim().isEmpty() || 
                dateField.getText().trim().isEmpty() || 
                timeField.getText().trim().isEmpty() || 
                stadiumIdField.getText().trim().isEmpty() || 
                statusField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please fill in all fields", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int matchId = Integer.parseInt(idField.getText().trim());
                int homeTeamId = Integer.parseInt(homeTeamField.getText().trim());
                int awayTeamId = Integer.parseInt(awayTeamField.getText().trim());
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                LocalTime time = LocalTime.parse(timeField.getText().trim());
                int stadiumId = Integer.parseInt(stadiumIdField.getText().trim());
                String status = statusField.getText().trim();
                
                // 验证主客队不能相同
                if (homeTeamId == awayTeamId) {
                    JOptionPane.showMessageDialog(frame, 
                        "Home team and away team cannot be the same", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 验证日期不能是过去的日期
                if (date.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(frame, 
                        "Match date cannot be in the past", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                outputArea.append("\n----- Adding New Match -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> FootballMatchOperations.insertMatch(matchId, homeTeamId, awayTeamId,
                    date, time, stadiumId, status));
                
                // 清空输入框
                idField.setText("");
                homeTeamField.setText("");
                awayTeamField.setText("");
                dateField.setText("");
                timeField.setText("");
                stadiumIdField.setText("");
                statusField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter valid numbers for ID fields", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter valid date (YYYY-MM-DD) and time (HH:mm:ss)", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addButton, gbc);

        // 删除面板
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Match"));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        deletePanel.add(new JLabel("Match ID to delete:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField deleteIdField = new JTextField(15);
        deletePanel.add(deleteIdField, gbc);

        JButton deleteButton = getjButton1(deleteIdField);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deletePanel.add(deleteButton, gbc);

        // 添加到操作面板
        operationsPanel.add(addPanel);
        operationsPanel.add(deletePanel);
        
        // 添加到主面板
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(operationsPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JButton getjButton1(JTextField deleteIdField) {
        JButton deleteButton = new JButton("Delete Match");
        deleteButton.addActionListener(e -> {
            if (deleteIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a Match ID to delete",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(deleteIdField.getText().trim());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(frame,
                        "Please enter a valid Match ID (positive number)",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete match with ID " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    outputArea.append("\n----- Deleting Match -----\n");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> FootballMatchOperations.deleteMatch(id));
                    deleteIdField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a valid Match ID (number only)",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        return deleteButton;
    }

    private JButton getButton(JTextField stadiumQueryField) {
        JButton nestedQueryButton = new JButton("Find Matches by Stadium");
        nestedQueryButton.addActionListener(e -> {
            String stadiumName = stadiumQueryField.getText().trim();
            if (!stadiumName.isEmpty()) {
                outputArea.append("\n----- Matches at Stadium: " + stadiumName + " -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> FootballMatchOperations.fetchMatchesByStadium(stadiumName));
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a stadium name");
            }
        });
        return nestedQueryButton;
    }

    private JPanel createSponsorPanel() {
        JPanel mainPanel = createBasePanel();
        
        // 创建顶部按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加查询按钮和嵌套查询
        JButton viewButton = new JButton("View All Sponsors");
        viewButton.addActionListener(e -> {
            outputArea.append("\n----- View All Sponsors -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, SponsorOperations::fetchSponsorDetails);
        });

        JButton groupButton = new JButton("Group Sponsors by Industry");
        groupButton.addActionListener(e -> {
            outputArea.append("\n----- Group Sponsors by Industry -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, SponsorOperations::groupSponsorsByIndustry);
        });
        
        // 嵌套查询面板
        JPanel nestedQueryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nestedQueryPanel.add(new JLabel("Industry:"));
        JTextField industryQueryField = new JTextField(15);
        JButton nestedQueryButton = getQueryButton(industryQueryField);

        nestedQueryPanel.add(industryQueryField);
        nestedQueryPanel.add(nestedQueryButton);

        buttonPanel.add(viewButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(nestedQueryPanel);

        // 创建中间的操作面板
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // 添加表单面板
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Sponsor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 标签列
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        addPanel.add(new JLabel("Sponsor ID:"), gbc);
        
        gbc.gridy = 1;
        addPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridy = 2;
        addPanel.add(new JLabel("Industry:"), gbc);
        
        gbc.gridy = 3;
        addPanel.add(new JLabel("Contact Person:"), gbc);
        
        // 输入框列
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField idField = new JTextField(15);
        addPanel.add(idField, gbc);
        
        gbc.gridy = 1;
        JTextField nameField = new JTextField(15);
        addPanel.add(nameField, gbc);
        
        gbc.gridy = 2;
        JTextField industryField = new JTextField(15);
        addPanel.add(industryField, gbc);
        
        gbc.gridy = 3;
        JTextField contactPersonField = new JTextField(15);
        addPanel.add(contactPersonField, gbc);
        
        // 添加按钮
        JButton addButton = new JButton("Add Sponsor");
        addButton.addActionListener(e -> {
            // 验证所有字段是否填写
            if (idField.getText().trim().isEmpty() || 
                nameField.getText().trim().isEmpty() || 
                industryField.getText().trim().isEmpty() || 
                contactPersonField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please fill in all fields", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int sponsorId = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                String industry = industryField.getText().trim();
                String contactPerson = contactPersonField.getText().trim();
                
                // 验证ID
                if (sponsorId <= 0) {
                    JOptionPane.showMessageDialog(frame, 
                        "Please enter a valid Sponsor ID (positive number)", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                outputArea.append("\n----- Adding New Sponsor -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> SponsorOperations.insertSponsor(sponsorId, name, industry, contactPerson));

                // 清空输入框
                idField.setText("");
                nameField.setText("");
                industryField.setText("");
                contactPersonField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter a valid number for Sponsor ID", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addButton, gbc);

        // 删除面板
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Sponsor"));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        deletePanel.add(new JLabel("Sponsor ID to delete:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField deleteIdField = new JTextField(15);
        deletePanel.add(deleteIdField, gbc);

        JButton deleteButton = getjButton2(deleteIdField);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deletePanel.add(deleteButton, gbc);

        // 添加到操作面板
        operationsPanel.add(addPanel);
        operationsPanel.add(deletePanel);
        
        // 添加到主面板
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(operationsPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JButton getjButton2(JTextField deleteIdField) {
        JButton deleteButton = new JButton("Delete Sponsor");
        deleteButton.addActionListener(e -> {
            if (deleteIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a Sponsor ID to delete",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(deleteIdField.getText().trim());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(frame,
                        "Please enter a valid Sponsor ID (positive number)",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete sponsor with ID " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    outputArea.append("\n----- Deleting Sponsor -----\n");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> SponsorOperations.deleteSponsor(id));
                    deleteIdField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a valid Sponsor ID (number only)",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        return deleteButton;
    }

    private JButton getQueryButton(JTextField industryQueryField) {
        JButton nestedQueryButton = new JButton("Find Sponsors by Industry");
        nestedQueryButton.addActionListener(e -> {
            String industry = industryQueryField.getText().trim();
            if (!industry.isEmpty()) {
                outputArea.append("\n----- Sponsors in Industry: " + industry + " -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> SponsorOperations.fetchSponsorsByIndustry(industry));
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter an industry");
            }
        });
        return nestedQueryButton;
    }

    private JPanel createContractPanel() {
        JPanel mainPanel = createBasePanel();
        
        // 创建顶部按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加查询按钮和嵌套查询
        JButton viewButton = new JButton("View All Contracts");
        viewButton.addActionListener(e -> {
            outputArea.append("\n----- View All Contracts -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, ContractOperations::fetchContractPlayerInfo);
        });

        JButton groupButton = new JButton("Group Contracts by Status");
        groupButton.addActionListener(e -> {
            outputArea.append("\n----- Group Contracts by Status -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, ContractOperations::groupContractsByStatus);
        });
        
        // 嵌套查询面板
        JPanel nestedQueryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nestedQueryPanel.add(new JLabel("Team Name:"));
        JTextField teamQueryField = new JTextField(15);
        JButton nestedQueryButton = getjButton3(teamQueryField);

        nestedQueryPanel.add(teamQueryField);
        nestedQueryPanel.add(nestedQueryButton);

        buttonPanel.add(viewButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(nestedQueryPanel);

        // 创建中间的操作面板
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // 添加表单面板
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Contract"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 标签列
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        addPanel.add(new JLabel("Contract ID:"), gbc);
        
        gbc.gridy = 1;
        addPanel.add(new JLabel("Player ID:"), gbc);
        
        gbc.gridy = 2;
        addPanel.add(new JLabel("Team ID:"), gbc);
        
        gbc.gridy = 3;
        addPanel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridy = 4;
        addPanel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridy = 5;
        addPanel.add(new JLabel("Value:"), gbc);
        
        gbc.gridy = 6;
        addPanel.add(new JLabel("Status:"), gbc);
        
        // 输入框列
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField idField = new JTextField(15);
        addPanel.add(idField, gbc);
        
        gbc.gridy = 1;
        JTextField playerIdField = new JTextField(15);
        addPanel.add(playerIdField, gbc);
        
        gbc.gridy = 2;
        JTextField teamIdField = new JTextField(15);
        addPanel.add(teamIdField, gbc);
        
        gbc.gridy = 3;
        JTextField startDateField = new JTextField(15);
        addPanel.add(startDateField, gbc);
        
        gbc.gridy = 4;
        JTextField endDateField = new JTextField(15);
        addPanel.add(endDateField, gbc);
        
        gbc.gridy = 5;
        JTextField valueField = new JTextField(15);
        addPanel.add(valueField, gbc);
        
        gbc.gridy = 6;
        JTextField statusField = new JTextField(15);
        addPanel.add(statusField, gbc);
        
        // 添加按钮
        JButton addButton = new JButton("Add Contract");
        addButton.addActionListener(e -> {
            // 验证所有字段是否填写
            if (idField.getText().trim().isEmpty() || 
                playerIdField.getText().trim().isEmpty() || 
                teamIdField.getText().trim().isEmpty() || 
                startDateField.getText().trim().isEmpty() || 
                endDateField.getText().trim().isEmpty() || 
                valueField.getText().trim().isEmpty() || 
                statusField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please fill in all fields", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int contractId = Integer.parseInt(idField.getText().trim());
                int playerId = Integer.parseInt(playerIdField.getText().trim());
                int teamId = Integer.parseInt(teamIdField.getText().trim());
                LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
                LocalDate endDate = LocalDate.parse(endDateField.getText().trim());
                double value = Double.parseDouble(valueField.getText().trim());
                String status = statusField.getText().trim();
                
                // 验证日期
                if (startDate.isAfter(endDate)) {
                    JOptionPane.showMessageDialog(frame, 
                        "Start date must be before end date", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 验证金额
                if (value <= 0) {
                    JOptionPane.showMessageDialog(frame, 
                        "Value must be positive", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                outputArea.append("\n----- Adding New Contract -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> ContractOperations.insertContract(contractId, playerId, teamId,
                    startDate, endDate, value, status));
                
                // 清空输入框
                idField.setText("");
                playerIdField.setText("");
                teamIdField.setText("");
                startDateField.setText("");
                endDateField.setText("");
                valueField.setText("");
                statusField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter valid numbers for ID, Player ID, Team ID and Value", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter valid dates in YYYY-MM-DD format", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addButton, gbc);

        // 删除面板
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Contract"));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        deletePanel.add(new JLabel("Contract ID to delete:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField deleteIdField = new JTextField(15);
        deletePanel.add(deleteIdField, gbc);

        JButton deleteButton = getjButton4(deleteIdField);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deletePanel.add(deleteButton, gbc);

        // 添加到操作面板
        operationsPanel.add(addPanel);
        operationsPanel.add(deletePanel);
        
        // 添加到主面板
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(operationsPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JButton getjButton4(JTextField deleteIdField) {
        JButton deleteButton = new JButton("Delete Contract");
        deleteButton.addActionListener(e -> {
            if (deleteIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a Contract ID to delete",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(deleteIdField.getText().trim());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(frame,
                        "Please enter a valid Contract ID (positive number)",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete contract with ID " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    outputArea.append("\n----- Deleting Contract -----\n");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> ContractOperations.deleteContract(id));
                    deleteIdField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter a valid Contract ID (number only)",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        return deleteButton;
    }

    private JButton getjButton3(JTextField teamQueryField) {
        JButton nestedQueryButton = new JButton("Find Contracts by Team");
        nestedQueryButton.addActionListener(e -> {
            String teamName = teamQueryField.getText().trim();
            if (!teamName.isEmpty()) {
                outputArea.append("\n----- Contracts for Team: " + teamName + " -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> ContractOperations.fetchContractsByTeam(teamName));
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a team name");
            }
        });
        return nestedQueryButton;
    }

    private JPanel createTeamSponsorPanel() {
        JPanel mainPanel = createBasePanel();
        
        // 创建顶部按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加查询按钮和嵌套查询
        JButton viewButton = new JButton("View All Sponsorships");
        viewButton.addActionListener(e -> {
            outputArea.append("\n----- View All Team Sponsorships -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, TeamSponsorOperations::fetchTeamSponsorDetails);
        });

        JButton groupButton = new JButton("Group Sponsorships by Industry");
        groupButton.addActionListener(e -> {
            outputArea.append("\n----- Group Sponsorships by Industry -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, TeamSponsorOperations::groupSponsorsByIndustry);
        });
        
        // 嵌套查询面板
        JPanel nestedQueryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nestedQueryPanel.add(new JLabel("Minimum Amount:"));
        JTextField amountQueryField = new JTextField(10);
        JButton nestedQueryButton = getjButton5(amountQueryField);

        nestedQueryPanel.add(amountQueryField);
        nestedQueryPanel.add(nestedQueryButton);

        buttonPanel.add(viewButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(nestedQueryPanel);

        // 创建中间的操作面板
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // 添加表单面板
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Team Sponsorship"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 标签列
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        addPanel.add(new JLabel("Team ID:"), gbc);
        
        gbc.gridy = 1;
        addPanel.add(new JLabel("Sponsor ID:"), gbc);
        
        gbc.gridy = 2;
        addPanel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridy = 3;
        addPanel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridy = 4;
        addPanel.add(new JLabel("Amount:"), gbc);
        
        // 输入框列
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField teamIdField = new JTextField(15);
        addPanel.add(teamIdField, gbc);
        
        gbc.gridy = 1;
        JTextField sponsorIdField = new JTextField(15);
        addPanel.add(sponsorIdField, gbc);
        
        gbc.gridy = 2;
        JTextField startDateField = new JTextField(15);
        addPanel.add(startDateField, gbc);
        
        gbc.gridy = 3;
        JTextField endDateField = new JTextField(15);
        addPanel.add(endDateField, gbc);
        
        gbc.gridy = 4;
        JTextField amountField = new JTextField(15);
        addPanel.add(amountField, gbc);
        
        // 添加按钮
        JButton addButton = new JButton("Add Sponsorship");
        addButton.addActionListener(e -> {
            // 验证所有字段是否填写
            if (teamIdField.getText().trim().isEmpty() || 
                sponsorIdField.getText().trim().isEmpty() || 
                startDateField.getText().trim().isEmpty() || 
                endDateField.getText().trim().isEmpty() || 
                amountField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please fill in all fields", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int teamId = Integer.parseInt(teamIdField.getText().trim());
                int sponsorId = Integer.parseInt(sponsorIdField.getText().trim());
                String startDate = startDateField.getText().trim();
                String endDate = endDateField.getText().trim();
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                
                // 验证ID和金额
                if (teamId <= 0 || sponsorId <= 0) {
                    JOptionPane.showMessageDialog(frame, 
                        "Please enter valid IDs (positive numbers)", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(frame, 
                        "Please enter a positive amount", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 验证日期格式和逻辑
                try {
                    Date.valueOf(startDate);
                    Date.valueOf(endDate);
                    if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                        JOptionPane.showMessageDialog(frame, 
                            "End date must be after start date", 
                            "Input Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, 
                        "Please enter valid dates in YYYY-MM-DD format", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                outputArea.append("\n----- Adding New Team Sponsorship -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> TeamSponsorOperations.insertTeamSponsor(teamId, sponsorId, startDate, endDate, amount));
                
                // 清空输入框
                teamIdField.setText("");
                sponsorIdField.setText("");
                startDateField.setText("");
                endDateField.setText("");
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter valid numbers for IDs and amount", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addButton, gbc);

        // 删除面板
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Team Sponsorship"));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        deletePanel.add(new JLabel("Team ID:"), gbc);
        
        gbc.gridy = 1;
        deletePanel.add(new JLabel("Sponsor ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField deleteTeamIdField = new JTextField(15);
        deletePanel.add(deleteTeamIdField, gbc);
        
        gbc.gridy = 1;
        JTextField deleteSponsorIdField = new JTextField(15);
        deletePanel.add(deleteSponsorIdField, gbc);

        JButton deleteButton = getjButton(deleteTeamIdField, deleteSponsorIdField);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deletePanel.add(deleteButton, gbc);

        // 添加到操作面板
        operationsPanel.add(addPanel);
        operationsPanel.add(deletePanel);
        
        // 添加到主面板
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(operationsPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JButton getjButton(JTextField deleteTeamIdField, JTextField deleteSponsorIdField) {
        JButton deleteButton = new JButton("Delete Sponsorship");
        deleteButton.addActionListener(e -> {
            if (deleteTeamIdField.getText().trim().isEmpty() ||
                deleteSponsorIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter both Team ID and Sponsor ID",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int teamId = Integer.parseInt(deleteTeamIdField.getText().trim());
                int sponsorId = Integer.parseInt(deleteSponsorIdField.getText().trim());

                if (teamId <= 0 || sponsorId <= 0) {
                    JOptionPane.showMessageDialog(frame,
                        "Please enter valid IDs (positive numbers)",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete this sponsorship?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    outputArea.append("\n----- Deleting Team Sponsorship -----\n");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> TeamSponsorOperations.deleteTeamSponsor(teamId, sponsorId));
                    deleteTeamIdField.setText("");
                    deleteSponsorIdField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                    "Please enter valid numbers for IDs",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        return deleteButton;
    }

    private JButton getjButton5(JTextField amountQueryField) {
        JButton nestedQueryButton = new JButton("Find Sponsorships by Amount");
        nestedQueryButton.addActionListener(e -> {
            try {
                BigDecimal minAmount = new BigDecimal(amountQueryField.getText().trim());
                if (minAmount.compareTo(BigDecimal.ZERO) > 0) {
                    outputArea.append("\n----- Sponsorships Above " + minAmount + " -----\n");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> TeamSponsorOperations.fetchSponsorsByAmount(minAmount));
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a positive amount");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number");
            }
        });
        return nestedQueryButton;
    }

}

// 用于重定向System.out到JTextArea的辅助类
class PrintStreamRedirector {
    public static void redirectSystemOut(JTextArea textArea, Runnable action) {
        PrintStream originalOut = System.out;
        TextAreaOutputStream textAreaOutputStream = new TextAreaOutputStream(textArea);
        System.setOut(new PrintStream(textAreaOutputStream));
        
        action.run();
        
        System.setOut(originalOut);
    }
}

class TextAreaOutputStream extends java.io.OutputStream {
    private final JTextArea textArea;
    private final StringBuilder buffer;
    
    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
        this.buffer = new StringBuilder();
    }
    
    @Override
    public void write(int b) {
        buffer.append((char) b);
        if (b == '\n') {
            textArea.append(buffer.toString());
            buffer.setLength(0);
        }
    }
    
    @Override
    public void flush() {
        if (!buffer.isEmpty()) {
            textArea.append(buffer.toString());
            buffer.setLength(0);
        }
    }
} 
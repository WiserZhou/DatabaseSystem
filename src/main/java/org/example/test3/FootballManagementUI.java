package org.example.test3;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
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

        // 创建选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // 添加输出区域
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);  // 启用自动换行
        outputArea.setWrapStyleWord(true);  // 按单词换行
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));  // 设置等宽字体
        
        // 创建滚动面板并设置
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputScrollPane.setPreferredSize(new Dimension(1200, 300));
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        // 添加各个操作面板
        tabbedPane.addTab("Team", createTeamPanel());
        tabbedPane.addTab("Player", createPlayerPanel());
        tabbedPane.addTab("Coach", createCoachPanel());
        tabbedPane.addTab("Stadium", createStadiumPanel());
        tabbedPane.addTab("Match", createMatchPanel());
        tabbedPane.addTab("Sponsor", createSponsorPanel());
        tabbedPane.addTab("Contract", createContractPanel());
        tabbedPane.addTab("Team Sponsor", createTeamSponsorPanel());

        // 添加组件到框架
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(outputScrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel createBasePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return buttonPanel;
    }

    private JPanel createFormPanel(String title) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return formPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return inputPanel;
    }

    private void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label + ": "), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JPanel createTeamPanel() {
        JPanel mainPanel = createBasePanel();
        
        // 创建顶部按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        // 添加查询按钮和嵌套查询
        JButton viewButton = new JButton("View All Teams");
        viewButton.addActionListener(e -> {
            outputArea.append("\n----- View All Teams -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, TeamOperations::fetchTeamDetails);
        });

        JButton groupButton = new JButton("Group Teams by Stadium Location");
        groupButton.addActionListener(e -> {
            outputArea.append("\n----- Group Teams by Stadium Location -----\n");
            PrintStreamRedirector.redirectSystemOut(outputArea, TeamOperations::groupTeamsByStadiumLocation);
        });
        
        // 嵌套查询面板使用FlowLayout紧凑布局
        JPanel nestedQueryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        nestedQueryPanel.add(new JLabel("Found Year:"));
        JTextField yearQueryField = new JTextField(6);
        JButton nestedQueryButton = new JButton("Find Teams by Found Year");
        nestedQueryButton.addActionListener(e -> {
            try {
                int year = Integer.parseInt(yearQueryField.getText());
                outputArea.append("\n----- Teams Founded in " + year + " -----\n");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                    TeamOperations.fetchTeamsByFoundYear(year);
                });
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid year");
            }
        });
        
        nestedQueryPanel.add(yearQueryField);
        nestedQueryPanel.add(nestedQueryButton);

        buttonPanel.add(viewButton);
        buttonPanel.add(groupButton);
        buttonPanel.add(nestedQueryPanel);

        // 创建中间的操作面板，使用紧凑的网格布局
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // 添加表单面板
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Team"));
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
        addPanel.add(new JLabel("Team Name:"), gbc);
        
        gbc.gridy = 2;
        addPanel.add(new JLabel("Found Year:"), gbc);
        
        gbc.gridy = 3;
        addPanel.add(new JLabel("Stadium ID:"), gbc);
        
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
        JTextField yearField = new JTextField(15);
        addPanel.add(yearField, gbc);
        
        gbc.gridy = 3;
        JTextField stadiumField = new JTextField(15);
        addPanel.add(stadiumField, gbc);
        
        // 添加按钮
        JButton addButton = new JButton("Add Team");
        addButton.addActionListener(e -> {
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
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                    TeamOperations.insertTeam(id, name, year, stadiumId);
                });
                
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
        });
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addButton, gbc);

        // 删除面板
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Team"));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        deletePanel.add(new JLabel("Team ID to delete:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField deleteIdField = new JTextField(15);
        deletePanel.add(deleteIdField, gbc);
        
        JButton deleteButton = new JButton("Delete Team");
        deleteButton.addActionListener(e -> {
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
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                        TeamOperations.deleteTeam(id);
                    });
                    deleteIdField.setText(""); // 清空输入框
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter a valid Team ID (number only)", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
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

    // 其他面板的创建方法类似，这里只展示部分代码
    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewButton = new JButton("View All Players");
        viewButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, PlayerOperations::fetchPlayerTeamInfo);
        });

        JButton groupButton = new JButton("Group Players by Nationality");
        groupButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, PlayerOperations::groupPlayersByNationality);
        });

        panel.add(viewButton);
        panel.add(groupButton);

        return panel;
    }

    private JPanel createCoachPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 查询按钮
        JButton viewButton = new JButton("View All Coaches");
        viewButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, CoachOperations::fetchCoachTeamInfo);
        });

        // 分组按钮
        JButton groupButton = new JButton("Group Coaches by Nationality");
        groupButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, CoachOperations::groupCoachesByNationality);
        });

        // 添加教练面板
        JPanel addPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Coach"));
        
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField nationalityField = new JTextField();
        JTextField licenseLevelField = new JTextField();
        JTextField teamIdField = new JTextField();
        
        addPanel.add(new JLabel("Coach ID:"));
        addPanel.add(idField);
        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Age:"));
        addPanel.add(ageField);
        addPanel.add(new JLabel("Nationality:"));
        addPanel.add(nationalityField);
        addPanel.add(new JLabel("License Level:"));
        addPanel.add(licenseLevelField);
        addPanel.add(new JLabel("Team ID:"));
        addPanel.add(teamIdField);
        
        JButton addButton = new JButton("Add Coach");
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String nationality = nationalityField.getText();
                String licenseLevel = licenseLevelField.getText();
                int teamId = Integer.parseInt(teamIdField.getText());
                
                outputArea.setText("");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                    CoachOperations.insertCoach(id, name, age, nationality, licenseLevel, teamId);
                });
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers");
            }
        });

        // 添加删除面板
        JPanel deletePanel = new JPanel(new GridLayout(0, 2, 5, 5));
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Coach"));
        
        JTextField deleteIdField = new JTextField();
        deletePanel.add(new JLabel("Coach ID to delete:"));
        deletePanel.add(deleteIdField);
        
        JButton deleteButton = new JButton("Delete Coach");
        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(deleteIdField.getText());
                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete coach with ID " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    outputArea.setText("");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                        CoachOperations.deleteCoach(id);
                    });
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid coach ID");
            }
        });

        panel.add(viewButton);
        panel.add(groupButton);
        panel.add(addPanel);
        panel.add(addButton);
        panel.add(deletePanel);
        panel.add(deleteButton);

        return panel;
    }

    private JPanel createStadiumPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewButton = new JButton("View All Stadiums");
        viewButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, StadiumOperations::fetchStadiumDetails);
        });

        JButton groupButton = new JButton("Group Stadiums by Location");
        groupButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, StadiumOperations::groupStadiumsByLocation);
        });

        // 添加球场面板
        JPanel addPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Stadium"));
        
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField buildYearField = new JTextField();
        
        addPanel.add(new JLabel("Stadium ID:"));
        addPanel.add(idField);
        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Capacity:"));
        addPanel.add(capacityField);
        addPanel.add(new JLabel("Location:"));
        addPanel.add(locationField);
        addPanel.add(new JLabel("Build Year:"));
        addPanel.add(buildYearField);
        
        JButton addButton = new JButton("Add Stadium");
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int capacity = Integer.parseInt(capacityField.getText());
                String location = locationField.getText();
                int buildYear = Integer.parseInt(buildYearField.getText());
                
                outputArea.setText("");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                    StadiumOperations.insertStadium(id, name, capacity, location, buildYear);
                });
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers");
            }
        });

        panel.add(viewButton);
        panel.add(groupButton);
        panel.add(addPanel);
        panel.add(addButton);

        return panel;
    }

    private JPanel createMatchPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewButton = new JButton("View All Matches");
        viewButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, FootballMatchOperations::fetchMatchDetails);
        });

        JButton groupButton = new JButton("Group Matches by Status");
        groupButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, FootballMatchOperations::groupMatchesByStatus);
        });

        // 添加比赛面板
        JPanel addPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Match"));
        
        JTextField matchIdField = new JTextField();
        JTextField homeTeamField = new JTextField();
        JTextField awayTeamField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField stadiumIdField = new JTextField();
        JTextField statusField = new JTextField();
        
        addPanel.add(new JLabel("Match ID:"));
        addPanel.add(matchIdField);
        addPanel.add(new JLabel("Home Team ID:"));
        addPanel.add(homeTeamField);
        addPanel.add(new JLabel("Away Team ID:"));
        addPanel.add(awayTeamField);
        addPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        addPanel.add(dateField);
        addPanel.add(new JLabel("Time (HH:mm:ss):"));
        addPanel.add(timeField);
        addPanel.add(new JLabel("Stadium ID:"));
        addPanel.add(stadiumIdField);
        addPanel.add(new JLabel("Status:"));
        addPanel.add(statusField);
        
        JButton addButton = new JButton("Add Match");
        addButton.addActionListener(e -> {
            try {
                int matchId = Integer.parseInt(matchIdField.getText());
                int homeTeamId = Integer.parseInt(homeTeamField.getText());
                int awayTeamId = Integer.parseInt(awayTeamField.getText());
                LocalDate date = LocalDate.parse(dateField.getText());
                LocalTime time = LocalTime.parse(timeField.getText());
                int stadiumId = Integer.parseInt(stadiumIdField.getText());
                String status = statusField.getText();
                
                outputArea.setText("");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                    FootballMatchOperations.insertMatch(matchId, homeTeamId, awayTeamId, 
                        date, time, stadiumId, status);
                });
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid data");
            }
        });

        // 添删除面板
        JPanel deletePanel = new JPanel(new GridLayout(0, 2, 5, 5));
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Match"));
        
        JTextField deleteIdField = new JTextField();
        deletePanel.add(new JLabel("Match ID to delete:"));
        deletePanel.add(deleteIdField);
        
        JButton deleteButton = new JButton("Delete Match");
        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(deleteIdField.getText());
                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete match with ID " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    outputArea.setText("");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                        FootballMatchOperations.deleteMatch(id);
                    });
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid match ID");
            }
        });

        panel.add(viewButton);
        panel.add(groupButton);
        panel.add(addPanel);
        panel.add(addButton);
        panel.add(deletePanel);
        panel.add(deleteButton);

        return panel;
    }

    private JPanel createSponsorPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewButton = new JButton("View All Sponsors");
        viewButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, SponsorOperations::fetchSponsorDetails);
        });

        JButton groupButton = new JButton("Group Sponsors by Industry");
        groupButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, SponsorOperations::groupSponsorsByIndustry);
        });

        // 添加赞助商面板
        JPanel addPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Sponsor"));
        
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField industryField = new JTextField();
        JTextField contactPersonField = new JTextField();
        
        addPanel.add(new JLabel("Sponsor ID:"));
        addPanel.add(idField);
        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Industry:"));
        addPanel.add(industryField);
        addPanel.add(new JLabel("Contact Person:"));
        addPanel.add(contactPersonField);
        
        JButton addButton = new JButton("Add Sponsor");
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String industry = industryField.getText();
                String contactPerson = contactPersonField.getText();
                
                outputArea.setText("");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                    SponsorOperations.insertSponsor(id, name, industry, contactPerson);
                });
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers");
            }
        });

        panel.add(viewButton);
        panel.add(groupButton);
        panel.add(addPanel);
        panel.add(addButton);

        return panel;
    }

    private JPanel createContractPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewButton = new JButton("View All Contracts");
        viewButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, ContractOperations::fetchContractPlayerInfo);
        });

        JButton groupButton = new JButton("Group Contracts by Status");
        groupButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, ContractOperations::groupContractsByStatus);
        });

        // 添加合同面板
        JPanel addPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Contract"));
        
        JTextField contractIdField = new JTextField();
        JTextField playerIdField = new JTextField();
        JTextField teamIdField = new JTextField();
        JTextField startDateField = new JTextField();
        JTextField endDateField = new JTextField();
        JTextField valueField = new JTextField();
        JTextField statusField = new JTextField();
        
        addPanel.add(new JLabel("Contract ID:"));
        addPanel.add(contractIdField);
        addPanel.add(new JLabel("Player ID:"));
        addPanel.add(playerIdField);
        addPanel.add(new JLabel("Team ID:"));
        addPanel.add(teamIdField);
        addPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        addPanel.add(startDateField);
        addPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        addPanel.add(endDateField);
        addPanel.add(new JLabel("Value:"));
        addPanel.add(valueField);
        addPanel.add(new JLabel("Status:"));
        addPanel.add(statusField);
        
        JButton addButton = new JButton("Add Contract");
        addButton.addActionListener(e -> {
            try {
                int contractId = Integer.parseInt(contractIdField.getText());
                int playerId = Integer.parseInt(playerIdField.getText());
                int teamId = Integer.parseInt(teamIdField.getText());
                LocalDate startDate = LocalDate.parse(startDateField.getText());
                LocalDate endDate = LocalDate.parse(endDateField.getText());
                double value = Double.parseDouble(valueField.getText());
                String status = statusField.getText();
                
                outputArea.setText("");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                    ContractOperations.insertContract(contractId, playerId, teamId, 
                        startDate, endDate, value, status);
                });
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid data");
            }
        });

        panel.add(viewButton);
        panel.add(groupButton);
        panel.add(addPanel);
        panel.add(addButton);

        return panel;
    }

    private JPanel createTeamSponsorPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewButton = new JButton("View All Team Sponsors");
        viewButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, TeamSponsorOperations::fetchTeamSponsorDetails);
        });

        JButton groupButton = new JButton("Group Sponsors by Industry");
        groupButton.addActionListener(e -> {
            outputArea.setText("");
            PrintStreamRedirector.redirectSystemOut(outputArea, TeamSponsorOperations::groupSponsorsByIndustry);
        });

        // 添加球队赞助商关系面板
        JPanel addPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Team Sponsor"));
        
        JTextField teamIdField = new JTextField();
        JTextField sponsorIdField = new JTextField();
        JTextField startDateField = new JTextField();
        JTextField endDateField = new JTextField();
        JTextField amountField = new JTextField();
        
        addPanel.add(new JLabel("Team ID:"));
        addPanel.add(teamIdField);
        addPanel.add(new JLabel("Sponsor ID:"));
        addPanel.add(sponsorIdField);
        addPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        addPanel.add(startDateField);
        addPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        addPanel.add(endDateField);
        addPanel.add(new JLabel("Amount:"));
        addPanel.add(amountField);
        
        JButton addButton = new JButton("Add Team Sponsor");
        addButton.addActionListener(e -> {
            try {
                int teamId = Integer.parseInt(teamIdField.getText());
                int sponsorId = Integer.parseInt(sponsorIdField.getText());
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();
                BigDecimal amount = new BigDecimal(amountField.getText());
                
                outputArea.setText("");
                PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                    TeamSponsorOperations.insertTeamSponsor(teamId, sponsorId, startDate, endDate, amount);
                });
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid data");
            }
        });

        // 添加删除面板
        JPanel deletePanel = new JPanel(new GridLayout(0, 2, 5, 5));
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Team Sponsor"));
        
        JTextField deleteTeamIdField = new JTextField();
        JTextField deleteSponsorIdField = new JTextField();
        deletePanel.add(new JLabel("Team ID:"));
        deletePanel.add(deleteTeamIdField);
        deletePanel.add(new JLabel("Sponsor ID:"));
        deletePanel.add(deleteSponsorIdField);
        
        JButton deleteButton = new JButton("Delete Team Sponsor");
        deleteButton.addActionListener(e -> {
            try {
                int teamId = Integer.parseInt(deleteTeamIdField.getText());
                int sponsorId = Integer.parseInt(deleteSponsorIdField.getText());
                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete team sponsor relationship?\nTeam ID: " + teamId + "\nSponsor ID: " + sponsorId,
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    outputArea.setText("");
                    PrintStreamRedirector.redirectSystemOut(outputArea, () -> {
                        TeamSponsorOperations.deleteTeamSponsor(teamId, sponsorId);
                    });
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid IDs");
            }
        });

        panel.add(viewButton);
        panel.add(groupButton);
        panel.add(addPanel);
        panel.add(addButton);
        panel.add(deletePanel);
        panel.add(deleteButton);

        return panel;
    }

    // 添加一个辅助方法来追加文本
    private void appendToOutput(String text) {
        outputArea.append(text);
        outputArea.setCaretPosition(outputArea.getDocument().getLength());  // 自动滚动到底部
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
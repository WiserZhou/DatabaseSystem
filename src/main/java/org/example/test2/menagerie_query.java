package org.example.test2;

import java.sql.*;
import java.util.*;

public class menagerie_query {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/menagerie?useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 提示用户输入查询编号
        System.out.println("Enter query number (1-9):");
        int queryNumber = scanner.nextInt();
        scanner.nextLine(); // 清空缓冲区

        List<String> params = new ArrayList<>();

        // 根据查询编号要求用户输入不同的参数
        switch (queryNumber) {
            case 1:
                System.out.println("Enter project number (PNO):");
                params.add(scanner.nextLine());
                break;
            case 2:
                System.out.println("Enter project name (PNAME):");
                params.add(scanner.nextLine());
                break;
            case 3:
                System.out.println("Enter department name (DNAME):");
                params.add(scanner.nextLine());
                break;
            case 4:
                System.out.println("Enter department name (DNAME):");
                params.add(scanner.nextLine());
                System.out.println("Enter salary limit (SALARY):");
                params.add(scanner.nextLine());
                break;
            case 5:
                System.out.println("Enter project number (PNO):");
                params.add(scanner.nextLine());
                break;
            case 6:
                System.out.println("Enter supervisor name (ENAME):");
                params.add(scanner.nextLine());
                break;
            case 7:
                System.out.println("Enter first project number (PNO1):");
                params.add(scanner.nextLine());
                System.out.println("Enter second project number (PNO2):");
                params.add(scanner.nextLine());
                break;
            case 8:
                System.out.println("Enter salary limit (SALARY):");
                params.add(scanner.nextLine());
                break;
            case 9:
                System.out.println("Enter number of projects (N):");
                params.add(scanner.nextLine());
                System.out.println("Enter maximum hours (HOURS):");
                params.add(scanner.nextLine());
                break;
            default:
                System.out.println("Invalid query number");
                return;
        }

        try {
            executeQuery(queryNumber, params);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    private static void executeQuery(int queryNumber, List<String> params) throws SQLException, ClassNotFoundException {
        // 注册 JDBC 驱动
        Class.forName(JDBC_DRIVER);
        // 打开链接
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();

        String sql = "";
        switch (queryNumber) {
            case 1:
                sql = "SELECT ESSN FROM WORKS_ON WHERE PNO = '" + params.getFirst() + "'";
                break;
            case 2:
                sql = "SELECT E.ENAME FROM EMPLOYEE E JOIN WORKS_ON W ON E.ESSN = W.ESSN JOIN PROJECT P ON W.PNO = P.PNO WHERE P.PNAME = '" + params.getFirst() + "'";
                break;
            case 3:
                sql = "SELECT ENAME, ADDRESS FROM EMPLOYEE E JOIN DEPARTMENT D ON E.DNO = D.DNO WHERE DNAME = '" + params.getFirst() + "'";
                break;
            case 4:
                sql = "SELECT ENAME, ADDRESS FROM EMPLOYEE E JOIN DEPARTMENT D ON E.DNO = D.DNO WHERE DNAME = '" + params.get(0) + "' AND SALARY < " + params.get(1);
                break;
            case 5:
                sql = "SELECT ENAME FROM EMPLOYEE WHERE ESSN NOT IN (SELECT ESSN FROM WORKS_ON WHERE PNO = '" + params.getFirst() + "')";
                break;
            case 6:
                sql = "SELECT E.ENAME, D.DNAME FROM EMPLOYEE E JOIN DEPARTMENT D ON E.DNO = D.DNO WHERE E.SUPERSSN = (SELECT ESSN FROM EMPLOYEE WHERE " +
                        "ENAME = '" + params.getFirst() + "')";
                break;
            case 7:
                sql = "SELECT ESSN FROM WORKS_ON WHERE PNO = '" + params.get(0) + "' INTERSECT SELECT ESSN FROM WORKS_ON WHERE PNO = '" + params.get(1) + "'";
                break;
            case 8:
                sql = "SELECT DNAME FROM DEPARTMENT D JOIN EMPLOYEE E ON D.DNO = E.DNO GROUP BY D.DNAME HAVING AVG(SALARY) < " + params.getFirst();
                break;
            case 9:
                sql = "SELECT ENAME FROM EMPLOYEE E JOIN (" +
                        "SELECT ESSN, COUNT(*) AS project_count, SUM(HOURS) AS total_hours FROM WORKS_ON GROUP BY ESSN) " +
                        "AS WORK_SUMMARY ON E.ESSN = WORK_SUMMARY.ESSN" +
                        " WHERE project_count >= " + params.get(0) + " AND total_hours <= " + params.get(1);
                break;
            default:
                System.out.println("Invalid query number");
                return;
        }

        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            // 这里根据查询不同，处理结果不同。例如：
            if (queryNumber == 3 || queryNumber == 4 || queryNumber == 6){
                System.out.println(rs.getString(1) + "   "+ rs.getString(2));
            }else{
                System.out.println(rs.getString(1));
            }
        }

        rs.close();
        stmt.close();
        conn.close();
    }
}

package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Administrator extends Database {

    private boolean isLogin;
    private String adminPassword;

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "ynuinfo#777";

    // 初始化数据库表
    public void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS admins (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "username TEXT NOT NULL, " +
                                "password TEXT NOT NULL)";
        createTable(createTableSQL);
    }

    // 插入默认管理员用户名和密码的方法
    public void insertDefaultAdmin() {
        String sql = "INSERT INTO admins (username, password) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, DEFAULT_ADMIN_USERNAME);
            preparedStatement.setString(2, DEFAULT_ADMIN_PASSWORD);
            preparedStatement.executeUpdate();
            System.out.println("默认管理员用户名和密码已成功插入到数据库admins中！");
        } catch (SQLException e) {
            System.out.println("插入默认管理员用户名和密码失败: " + e.getMessage());
        }
    }

    // 管理员登录
    public boolean loginAdmin(Scanner scanner) {
        System.out.println("输入用户名:");
        String username = scanner.next();
        System.out.println("输入密码:");
        String password = scanner.next();
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                isLogin = true;
                adminPassword = password;
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // 管理员登出
    public void logoutAdmin() {
        isLogin = false;
    }
}

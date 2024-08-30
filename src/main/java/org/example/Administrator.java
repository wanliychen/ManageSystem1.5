package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Statement;

/// 管理员的登录、登出、修改密码，以及重置特定用户的密码
public class Administrator {

    boolean isLogin ;
    String adminPassword ;
    private static final String DB_URL = "jdbc:sqlite:users.db";

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "ynuinfo#777";

    // 新建一个表
    public void createTable() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS admins (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "username TEXT NOT NULL, " +
                         "password TEXT NOT NULL)";

            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("创建表时发生SQL错误: " + e.getMessage());
        }
    }

    // 插入默认管理员用户名和密码的方法
    public void insertDefaultAdmin() {
        // 使用try-with-resources语句来确保连接和语句在使用后自动关闭
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO admins (username, password) VALUES (?, ?)")) {
            // 设置参数
            preparedStatement.setString(1, DEFAULT_ADMIN_USERNAME);
            preparedStatement.setString(2, DEFAULT_ADMIN_PASSWORD);
            // 执行插入操作
            preparedStatement.executeUpdate();
            // 输出插入成功的消息
            System.out.println("默认管理员用户名和密码已成功插入到数据库admins中！");
        } catch (SQLException e) {
            // 捕获并输出SQL异常信息
            System.out.println("插入默认管理员用户名和密码失败: " + e.getMessage());
        }
    }

    public boolean loginAdmin(Scanner scanner) {
        System.out.println("输入用户名:");
        String username=scanner.next();
        System.out.println("输入密码:");
        String password=scanner.next();
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
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

    public void logoutAdmin() {
        isLogin = false;
    }

}

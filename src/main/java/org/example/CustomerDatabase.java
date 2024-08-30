package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDatabase {
    private static final String DB_URL = "jdbc:sqlite:users.db";

    // 初始化数据库连接
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // 初始化数据库的方法
    public void initializeDatabase() {
        // 使用try-with-resources语句来确保连接和语句在使用后自动关闭
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            // 创建表的SQL查询语句，如果表不存在则创建
            String createTableQuery = "CREATE TABLE IF NOT EXISTS customers (" +
                                      "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                      "username TEXT NOT NULL, " +
                                      "password TEXT NOT NULL, " +
                                      "useremail TEXT NOT NULL, " +
                                      "phone TEXT NOT NULL, " +
                                      "registrationDate DATE NOT NULL, " +
                                      "userLevel TEXT NOT NULL)";
            // 执行SQL查询
            statement.executeUpdate(createTableQuery);
            // 输出初始化成功的消息
            System.out.println("数据库customers初始化成功！");
        } catch (SQLException e) {
            // 捕获并输出SQL异常信息
            System.out.println("数据库customers初始化失败: " + e.getMessage());
        }
    }

    // 增加用户信息
    public static boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers(username, password, useremail, phone, registrationDate, userLevel) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getUsername());
            pstmt.setString(2, customer.getUserpassword());
            pstmt.setString(3, customer.getUseremail());
            pstmt.setString(4, customer.getPhone());
            pstmt.setDate(5, new java.sql.Date(customer.getRegistrationDate().getTime()));
            pstmt.setString(6, customer.getUserLevel());
            pstmt.executeUpdate();
            System.out.println("增加用户成功！");
            return true;
        } catch (SQLException e) {
            System.out.println("增加用户失败:"+e.getMessage());
            return false;
            
        }
    }

    // 删除用户信息
    public static void deleteCustomerByUsername(String username) {
        String sql = "DELETE FROM customers WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 查找用户信息
    public static Customer findCustomerByUsername(String username) {
        String sql = "SELECT * FROM customers WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("useremail"),
                        rs.getString("phone"),
                        rs.getDate("registrationDate"),
                        rs.getString("userLevel")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // 更新用户信息
    public static void updateCustomer(String username, Customer updatedCustomer) {
        String sql = "UPDATE customers SET password = ?, useremail = ?, phone = ?, registrationDate = ?, userLevel = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updatedCustomer.getUserpassword());
            pstmt.setString(2, updatedCustomer.getUseremail());
            pstmt.setString(3, updatedCustomer.getPhone());
            pstmt.setDate(4, new java.sql.Date(updatedCustomer.getRegistrationDate().getTime()));
            pstmt.setString(5, updatedCustomer.getUserLevel());
            pstmt.setString(6, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 得到用户列表
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("useremail"),
                        rs.getString("phone"),
                        rs.getDate("registrationDate"),
                        rs.getString("userLevel")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }
}

package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDatabase extends Database {

    // 实现父类的数据库初始化方法
    @Override
    public void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS customers (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "username TEXT NOT NULL, " +
                                "password TEXT NOT NULL, " +
                                "useremail TEXT NOT NULL, " +
                                "phone TEXT NOT NULL, " +
                                "registrationDate DATE NOT NULL, " +
                                "userLevel TEXT NOT NULL)";
        createTable(createTableSQL);
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

package org.example;

import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class CustomerPasswordManage {
    private static final String DB_URL = "jdbc:sqlite:users.db";

    private Scanner scanner = new Scanner(System.in);


    public void changePassword() {
        System.out.println("输入用户名：");
        String username = scanner.nextLine();
        System.out.println("输入旧密码：");
        String oldPassword = scanner.nextLine();
        System.out.println("输入新密码：");
        String newPassword = scanner.nextLine();

        if (isValidPassword(newPassword)) {
            if (updatePassword(username, oldPassword, newPassword)) {
                System.out.println("密码修改成功！");
            } else {
                System.out.println("旧密码不正确，密码修改失败！");
            }
        } else {
            System.out.println("新密码不符合要求，密码修改失败！");
        }
    }


    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    private boolean updatePassword(String username, String oldPassword, String newPassword) {
        String sql = "UPDATE customers SET password = ? WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.setString(3, oldPassword);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    
    void resetPassword(String username) {
       
        System.out.println("输入注册邮箱：");
        String email = scanner.nextLine();
        String newPassword = generateRandomPassword();
        if (isEmailCorrect(username, email)) {
            sendPasswordToEmail(email, newPassword);
            System.out.println("新密码已发送到您的邮箱，请查收！");
        } else {
            System.out.println("用户名或邮箱不正确，密码重置失败！");
        }
    }
   
    private boolean isEmailCorrect(String username, String email) {
        String sql = "SELECT useremail FROM customers WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedEmail = rs.getString("useremail");
                return storedEmail.equals(email);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        Random rnd = new Random();
        while (password.length() < 8) { // 
            int index = (int) (rnd.nextFloat() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }

    private void sendPasswordToEmail(String email, String password) {
        // 模拟发送邮件功能
        System.out.println("模拟发送邮件到 " + email + "，新密码为：" + password);
    }
}

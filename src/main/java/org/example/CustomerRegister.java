package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Scanner;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomerRegister {
    private static final String DB_URL = "jdbc:sqlite:users.db";

    private Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("请输入用户名：");
        String username = scanner.nextLine();
        System.out.println("请输入密码：");
        String password = scanner.nextLine();
        System.out.println("请输入邮箱：");
        String email = scanner.nextLine();
        System.out.println("请输入电话号码：");
        String phone = scanner.nextLine();
        System.out.println("请输入注册日期（格式：yyyy-MM-dd）：");
        String registrationDateStr = scanner.nextLine();
        Date registrationDate = Date.valueOf(registrationDateStr);
        System.out.println("请输入用户等级：");
        String userLevel = scanner.nextLine();

        if (isValidUsername(username) && isValidPassword(password) && isValidEmail(email)) {
            if (registerCustomer(username, password, email, phone, registrationDate, userLevel)) {
                System.out.println("注册成功！");
            } else {
                System.out.println("注册失败，用户名已存在！");
            }
        } else {
            System.out.println("用户名、密码或邮箱不符合要求，注册失败！");
        }
    }

    private boolean isValidUsername(String username) {
        return username.length() >= 5;
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

    private boolean isValidEmail(String email) {
        // 简单的邮箱格式验证
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean registerCustomer(String username, String password, String email, String phone, Date registrationDate, String userLevel) {
        String hashPassword=hashPassword(password);
        String sql = "INSERT INTO customers (username, password, useremail, phone, registrationDate, userLevel) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setDate(5, registrationDate);
            pstmt.setString(6, userLevel);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    private static String hashPassword(String password) {//使用MD5加密
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

}

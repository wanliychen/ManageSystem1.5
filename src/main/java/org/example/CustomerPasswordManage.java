package org.example;

import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomerPasswordManage {
    private static final String DB_URL = "jdbc:sqlite:users.db";
    private Scanner scanner = new Scanner(System.in);

    // 修改密码
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

    // 验证新密码的有效性
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

    // 更新密码
    private boolean updatePassword(String username, String oldPassword, String newPassword) {
        String sql = "UPDATE customers SET password = ? WHERE username = ? AND password = ?";
        String hashedNewPassword = hashPassword(newPassword);
        String hashedOldPassword = hashPassword(oldPassword);
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashedNewPassword);
            pstmt.setString(2, username);
            pstmt.setString(3, hashedOldPassword);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("数据库错误: " + e.getMessage());
            return false;
        }
    }

    

    // 重置密码
    public void resetPassword(String username) {
        System.out.println("输入注册邮箱：");
        String email = scanner.nextLine();
        String newPassword = generateRandomPassword();
        String hashedPassword = hashPassword(newPassword);

        if (isEmailCorrect(username, email)) {
            //更新密码
            String sql = "UPDATE customers SET password = ? WHERE username = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, hashedPassword);
                pstmt.setString(2, username);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("数据库错误: " + e.getMessage());
            }
            sendPasswordToEmail(email, newPassword);
        } else {
            System.out.println("用户名或邮箱不正确，密码重置失败！");
        }
    }

    // 检查邮箱是否正确
    private boolean isEmailCorrect(String username, String email) {
        String sql = "SELECT useremail FROM customers WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedEmail = rs.getString("useremail");
                return storedEmail.equalsIgnoreCase(email);
            }
        } catch (SQLException e) {
            System.out.println("数据库错误: " + e.getMessage());
        }
        return false;
    }

    // 生成随机密码
    private String generateRandomPassword() {
        String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
        String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String DIGITS = "0123456789";
        String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        password.append(LOWERCASE_CHARS.charAt(random.nextInt(LOWERCASE_CHARS.length())));
        password.append(UPPERCASE_CHARS.charAt(random.nextInt(UPPERCASE_CHARS.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        for (int i = 4; i < 10; i++) {
            String allChars = LOWERCASE_CHARS + UPPERCASE_CHARS + DIGITS + SPECIAL_CHARS;
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
    }

    // 使用MD5加密密码
    private static String hashPassword(String password) {
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

    // 模拟发送密码到邮箱
    private void sendPasswordToEmail(String email, String password) {
        System.out.println("新密码已发送至邮箱 " + email + "，新密码为：" + password);
    }
}

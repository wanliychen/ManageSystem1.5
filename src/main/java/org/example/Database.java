package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
    private static final String DB_URL = "jdbc:sqlite:users.db";

    // 提供一个获取数据库连接的方法
    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // 子类可以根据需要实现数据库初始化
    public abstract void initializeDatabase();

    // 用于创建表的通用方法
    protected void createTable(String createTableSQL) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("表创建成功！");
        } catch (SQLException e) {
            System.out.println("表创建失败：" + e.getMessage());
        }
    }
}

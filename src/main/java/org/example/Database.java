// package org.example;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;
// import java.sql.Statement;

// public abstract class Database {
//     private static final String DB_URL = "jdbc:sqlite:users.db";

//     // 提供一个获取数据库连接的方法
//     protected static Connection getConnection() throws SQLException {
//         return DriverManager.getConnection(DB_URL);
//     }

//     // 子类可以根据需要实现数据库初始化
//     public abstract void initializeDatabase();

//     // 用于创建表的通用方法
//     protected void createTable(String createTableSQL) {
//         try (Connection conn = getConnection();
//              Statement stmt = conn.createStatement()) {
//             stmt.execute(createTableSQL);
//             System.out.println("表创建成功！");
//         } catch (SQLException e) {
//             System.out.println("表创建失败：" + e.getMessage());
//         }
//     }
// }


package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteErrorCode;

public abstract class Database {
    private static final String DB_URL = "jdbc:sqlite:users.db";
    private static final int MAX_RETRIES = 5; // 重试次数
    private static final int RETRY_DELAY_MS = 1000; // 重试延时，单位为毫秒

    // 提供一个获取数据库连接的方法，带重试机制
    protected static Connection getConnection() throws SQLException {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                Connection conn = DriverManager.getConnection(DB_URL);
                // 启用 WAL 模式
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA journal_mode=WAL;");
                }
                return conn;
            } catch (SQLException e) {
                if (e.getErrorCode() == SQLiteErrorCode.SQLITE_BUSY.code) {
                    retries++;
                    System.out.println("数据库锁定，重试中...");
                    try {
                        Thread.sleep(RETRY_DELAY_MS); // 等待一段时间后重试
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt(); // 处理中断
                        throw new SQLException("重试被中断", ie);
                    }
                } else {
                    throw e; // 非锁定错误，抛出异常
                }
            }
        }
        throw new SQLException("无法连接到数据库，数据库被锁定");
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
            e.printStackTrace();
        }
    }
}

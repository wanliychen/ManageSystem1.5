package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDatabase {
    private static final String DB_URL = "jdbc:sqlite:users.db";

    // 初始化数据库连接
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // 初始化数据库和创建表
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // 创建 products 表
            String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
                    "productId INTEGER PRIMARY KEY, " +
                    "productName TEXT NOT NULL, " +
                    "manufacturer TEXT NOT NULL, " +
                    "model TEXT NOT NULL, " +
                    "purchasePrice REAL NOT NULL, " +
                    "retailPrice REAL NOT NULL, " +
                    "nums INTEGER NOT NULL" +
                    ")";
            stmt.execute(createTableSQL);
            System.out.println("数据库products初始化成功！");
        } catch (SQLException e) {
            System.out.println("数据库products初始化失败" +e.getMessage());
        }
    }

    // 增加商品
    public static void addProduct(Product product) {
        String sql = "INSERT INTO products(productId, productName, manufacturer, model, purchasePrice, retailPrice, nums) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, product.getProductId());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getManufacturer());
            pstmt.setString(4, product.getModel());
            pstmt.setDouble(5, product.getPurchasePrice());
            pstmt.setDouble(6, product.getRetailPrice());
            pstmt.setInt(7, product.getNums());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 删除商品
    public static void deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 查找商品（通过ID）
    public static Product findProductById(int productId) {
        String sql = "SELECT * FROM products WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int productIdResult = rs.getInt("productId");
                String productName = rs.getString("productName");
                String manufacturer = rs.getString("manufacturer");
                String model = rs.getString("model");
                double purchasePrice = rs.getDouble("purchasePrice");
                double retailPrice = rs.getDouble("retailPrice");
                int nums = rs.getInt("nums");

                return new Product(productIdResult, productName, manufacturer, model, purchasePrice, retailPrice, nums);
            }
        } catch (SQLException e) {
            System.out.println("Error finding product: " + e.getMessage());
        }
        return null;
    }

    // 更新商品
    public static void updateProduct(int productId, Product updatedProduct) {
        String sql = "UPDATE products SET productName = ?, manufacturer = ?,  model = ?, purchasePrice = ?, retailPrice = ?, nums = ? WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updatedProduct.getProductName());
            pstmt.setString(2, updatedProduct.getManufacturer());
            pstmt.setString(3, updatedProduct.getModel());
            pstmt.setDouble(4, updatedProduct.getPurchasePrice());
            pstmt.setDouble(5, updatedProduct.getRetailPrice());
            pstmt.setInt(6, updatedProduct.getNums());
            pstmt.setInt(7, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 返回商品的集合
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("productId");
                String productName = rs.getString("productName");
                String manufacturer = rs.getString("manufacturer");
                String model = rs.getString("model");
                double purchasePrice = rs.getDouble("purchasePrice");
                double retailPrice = rs.getDouble("retailPrice");
                int nums = rs.getInt("nums");

                products.add(new Product(productId, productName, manufacturer, model, purchasePrice, retailPrice, nums));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }

    // 更新商品库存数量（减少）
    public void updateProductQuantity(int productId, int quantity) {
        String sql = "UPDATE products SET nums = nums - ? WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    
}

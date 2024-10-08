package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDatabase extends Database {

    // 实现父类的数据库初始化方法
    @Override
    public void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
                                "productId TEXT PRIMARY KEY, " +
                                "productName TEXT NOT NULL, " +
                                "manufacturer TEXT NOT NULL, " +
                                "model TEXT NOT NULL, " +
                                "purchasePrice REAL NOT NULL, " +
                                "retailPrice REAL NOT NULL, " +
                                "nums INTEGER NOT NULL)";
        createTable(createTableSQL);
    }

    // 增加商品
    public static void addProduct(Product product) {
        String sql = "INSERT INTO products(productId, productName, manufacturer, model, purchasePrice, retailPrice, nums) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getProductId()); 
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getManufacturer());
            pstmt.setString(4, product.getModel());
            pstmt.setDouble(5, product.getPurchasePrice());
            pstmt.setDouble(6, product.getRetailPrice());
            pstmt.setInt(7, product.getNums());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("添加商品失败：" + e.getMessage());
        }
    }


    public static void deleteProduct(String productId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("您确定要删除商品 " +productId + " 吗？该操作不可撤销。 (y/n)");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("y")) {
            System.out.println("删除操作已取消。");
            return; // 取消删除操作
        }

        String sql = "DELETE FROM products WHERE productId = ?";
        try (Connection conn = getConnection()) {
            // 开始事务
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, productId);
                pstmt.executeUpdate();
                // 提交事务
                conn.commit();
            } catch (SQLException e) {
                // 如果发生异常，回滚事务
                conn.rollback();
                System.out.println("删除商品失败：" + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("数据库操作失败：" + e.getMessage());
        }
    }
    

    // 查找商品（通过ID）
    public static Product findProductById(String productId) {  // 修改参数类型为 String
        String sql = "SELECT * FROM products WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);  // 修改为 setString
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String productIdResult = rs.getString("productId");  // 修改为 getString
                String productName = rs.getString("productName");
                String manufacturer = rs.getString("manufacturer");
                String model = rs.getString("model");
                double purchasePrice = rs.getDouble("purchasePrice");
                double retailPrice = rs.getDouble("retailPrice");
                int nums = rs.getInt("nums");

                return new Product(productIdResult, productName, manufacturer, model, purchasePrice, retailPrice, nums);
            }
        } catch (SQLException e) {
            System.out.println("查找商品失败：" + e.getMessage());
        }
        return null;
    }

    // 更新商品
    public static void updateProduct(String productId, Product updatedProduct) {  // 修改参数类型为 String
        String sql = "UPDATE products SET productName = ?, manufacturer = ?, model = ?, purchasePrice = ?, retailPrice = ?, nums = ? WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updatedProduct.getProductName());
            pstmt.setString(2, updatedProduct.getManufacturer());
            pstmt.setString(3, updatedProduct.getModel());
            pstmt.setDouble(4, updatedProduct.getPurchasePrice());
            pstmt.setDouble(5, updatedProduct.getRetailPrice());
            pstmt.setInt(6, updatedProduct.getNums());
            pstmt.setString(7, productId);  // 修改为 setString
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("更新商品失败：" + e.getMessage());
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
                String productId = rs.getString("productId");  // 修改为 getString
                String productName = rs.getString("productName");
                String manufacturer = rs.getString("manufacturer");
                String model = rs.getString("model");
                double purchasePrice = rs.getDouble("purchasePrice");
                double retailPrice = rs.getDouble("retailPrice");
                int nums = rs.getInt("nums");

                products.add(new Product(productId, productName, manufacturer, model, purchasePrice, retailPrice, nums));
            }
        } catch (SQLException e) {
            System.out.println("获取商品失败：" + e.getMessage());
        }
        return products;
    }

    // 更新商品库存数量
    public void updateProductQuantity(String productId, int quantity) {  // 修改参数类型为 String
        String sql = "UPDATE products SET nums = nums - ? WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setString(2, productId);  // 修改为 setString
            pstmt.executeUpdate();

            // 检查更新后的库存数量是否 <= 0，若是则删除该商品
            String checkStockSql = "SELECT nums FROM products WHERE productId = ?";
            try (PreparedStatement pstmtCheckStock = conn.prepareStatement(checkStockSql)) {
                pstmtCheckStock.setString(1, productId);
                ResultSet rs = pstmtCheckStock.executeQuery();
                if (rs.next()) {
                    int remainingStock = rs.getInt("nums");
                    if (remainingStock <= 0) {
                        deleteProduct(productId);  // 删除库存为 0 或负数的商品
                        System.out.println("商品ID " + productId + " 已从库存中删除（库存不足）。");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("更新库存失败：" + e.getMessage());
        }
    }
}

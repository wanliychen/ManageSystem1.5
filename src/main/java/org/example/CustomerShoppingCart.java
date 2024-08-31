package org.example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CustomerShoppingCart {
    private Map<String, Integer> shoppingCart; // 商品ID和数量的映射
    private ProductDatabase productDatabase;
    private List<Map<String, Integer>> purchaseHistoryList; // 保存购物历史
    private Scanner scanner = new Scanner(System.in);

    public CustomerShoppingCart(Customer customer) {
        this.shoppingCart = new HashMap<>();
        this.productDatabase = new ProductDatabase();
        this.purchaseHistoryList = new ArrayList<>(); // 初始化购物历史
    }

    // 将商品加入购物车
    public void addToCart() {
        System.out.println("请输入商品ID：");
        String productId = scanner.nextLine();
        System.out.println("请输入数量：");
        int quantity = 0;
        try {
            quantity = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
        } catch (InputMismatchException e) {
            System.out.println("输入的数量无效，请输入一个整数。");
            scanner.nextLine(); // 清空输入流
            return;
        }

        if (productDatabase.findProductById(Integer.parseInt(productId)) != null) {
            shoppingCart.put(productId, shoppingCart.getOrDefault(productId, 0) + quantity);
            System.out.println("商品成功加入购物车！");
        } else {
            System.out.println("未找到商品ID " + productId + " 对应的商品。");
        }
    }

    // 将商品从购物车中移除
    public void removeFromCart() {
        System.out.println("请输入要移除的商品ID：");
        String productId = scanner.nextLine();
        
        if (!shoppingCart.containsKey(productId)) {
            System.out.println("购物车中未找到商品ID " + productId);
            return;
        }

        System.out.println("确认要移除商品ID " + productId + " 吗？(yes/no)");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes")) {
            shoppingCart.remove(productId);
            System.out.println("商品已从购物车中移除。");
        } else {
            System.out.println("取消移除操作。");
        }
    }

    // 修改购物车中的商品数量
    public void updateCartItemQuantity() {
        System.out.println("请输入商品ID：");
        String productId = scanner.nextLine();
        if (!shoppingCart.containsKey(productId)) {
            System.out.println("购物车中未找到商品ID " + productId);
            return;
        }

        System.out.println("请输入新的数量：");
        int newQuantity = 0;
        try {
            newQuantity = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
        } catch (InputMismatchException e) {
            System.out.println("输入的数量无效，请输入一个整数。");
            scanner.nextLine(); // 清空输入流
            return;
        }

        if (newQuantity <= 0) {
            shoppingCart.remove(productId);
            System.out.println("商品已从购物车中移除。");
        } else {
            shoppingCart.put(productId, newQuantity);
            System.out.println("商品数量已更新。");
        }
    }

    // 模拟结账
    public void checkout() {
        if (shoppingCart.isEmpty()) {
            System.out.println("购物车为空，无法结账。");
            return;
        }

        System.out.println("请选择支付方式：");
        System.out.println("1. 支付宝");
        System.out.println("2. 微信");
        System.out.println("3. 银行卡");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                System.out.println("使用支付宝支付...");
                break;
            case 2:
                System.out.println("使用微信支付...");
                break;
            case 3:
                System.out.println("使用银行卡支付...");
                break;
            default:
                System.out.println("无效的支付方式。");
                return;
        }

        // 模拟支付成功后更新商品库存
        for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
            int productId = Integer.parseInt(entry.getKey());
            int quantity = entry.getValue();
            productDatabase.updateProductQuantity(productId, quantity);
        }

        // 保存购买记录
        purchaseHistoryList.add(new HashMap<>(shoppingCart));

        // 清空购物车
        shoppingCart.clear();
        System.out.println("结账成功，购物车已清空！");
    }

   // 查看购物历史
    public void getPurchaseHistory() {
        if (purchaseHistoryList.isEmpty()) {
            System.out.println("暂无购物历史。");
            return;
        }
        System.out.println("您的购物历史：");
        for (Map<String, Integer> history : purchaseHistoryList) {
            // 获取当前北京时间
            ZonedDateTime beijingTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));

            // 使用包含日期和时间的 ZonedDateTime 对象
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = beijingTime.format(formatter);

            System.out.println("购物时间: " + formattedDateTime);
            System.out.println("商品清单: " + history);
        }
    }
}

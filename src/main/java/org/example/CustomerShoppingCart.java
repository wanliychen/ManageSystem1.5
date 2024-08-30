package org.example;

import java.util.*;

public class CustomerShoppingCart {
    private Map<String, Integer> shoppingCart; // 商品集
    private ProductDatabase productDatabase;
    Scanner scanner=new Scanner(System.in);

    public CustomerShoppingCart(Customer customer) {
        this.shoppingCart = new HashMap<>();
        this.productDatabase = new ProductDatabase();
    }

    // 加入购物车
    public void addToCart() {
        System.out.println("请输入商品ID：");
        String productId = scanner.nextLine();
        System.out.println("请输入数量：");
        int quantity = 0;
        try {
            quantity = scanner.nextInt();
            scanner.nextLine(); // 消耗换行
        } catch (InputMismatchException e) {
            System.out.println("输入的数量无效，请输入一个整数。");
            return; // 结束方法，不继续执行
        }


        if (productDatabase.findProductById(Integer.parseInt(productId)) != null) {
            shoppingCart.put(productId, shoppingCart.getOrDefault(productId, 0) + quantity);
            productDatabase.updateProductQuantity(Integer.parseInt(productId),quantity);
            System.out.println("添加成功");
        } else {
            System.out.println("Product ID " + productId + " not found in the database.");
        }
    }

    // 从购物车删除
    public void removeFromCart() {
        System.out.println("请输入商品ID：");
        String productId = scanner.nextLine();
        System.out.println("请输入数量：");
        int quantity = 0;
        try {
            quantity = scanner.nextInt();
            scanner.nextLine(); // 消耗换行
        } catch (InputMismatchException e) {
            System.out.println("输入的数量无效，请输入一个整数。");
            return; // 结束方法，不继续执行
        }

        if (shoppingCart.containsKey(productId)) {
            int currentQuantity = shoppingCart.get(productId);
            if (currentQuantity > quantity) {
                shoppingCart.put(productId, currentQuantity - quantity);
            } else {
                shoppingCart.remove(productId);
            }
            productDatabase.increaseProductQuantity(Integer.parseInt(productId), quantity);
            System.out.println("删除成功");
        } else {
            System.out.println("Product ID " + productId + " not found in the cart.");
        }
    }

    // 更新购物车商品数量
    public void updateCartItemQuantity() {
         System.out.println("请输入商品ID：");
        String productId = scanner.nextLine();
        System.out.println("请输入数量：");
        int newQuantity = 0;
        try {
            newQuantity = scanner.nextInt();
            scanner.nextLine(); // 消耗换行
        } catch (InputMismatchException e) {
            System.out.println("输入的数量无效，请输入一个整数。");
            return; // 结束方法，不继续执行
        }
        if (shoppingCart.containsKey(productId)) {
            int currentQuantity = shoppingCart.get(productId);
            int quantityChange = newQuantity - currentQuantity;
            shoppingCart.put(productId, newQuantity);
            productDatabase.updateProductQuantity(Integer.parseInt(productId), quantityChange);
            System.out.println("更新成功");
        } else {
            System.out.println("Product ID " + productId + " not found in the cart.");
        }
    }

    // 获取购物车内容
    public void getShoppingCartHistory() {
        StringBuilder cart = new StringBuilder();
        for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
            cart.append("Product ID: ").append(entry.getKey())
                .append(", Quantity: ").append(entry.getValue())
                .append("\n");
        }
        System.out.println(cart.toString());
    }
   

    // 结账
    public void checkout(){
        System.out.println("请选择支付方式：");
        System.out.println("1. 支付宝");
        System.out.println("2. 微信");
        System.out.println("3. 银行卡");

        int choice=scanner.nextInt();
        scanner.nextLine();
        
        switch(choice){
            case 1:
                System.out.println("使用支付宝支付");
                break;
            case 2:
                System.out.println("使用微信支付");
                break;
            case 3:
                System.out.println("使用银行卡支付");
                break;
            default:
                System.out.println("无效的支付方式");
        }
    }
}
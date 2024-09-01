package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("欢迎使用购物管理系统");
        // admins数据库
        Administrator admin = new Administrator();
        admin.initializeDatabase();
        admin.insertDefaultAdmin();
        
        // products数据库
        ProductDatabase productDatabase=new ProductDatabase();
        productDatabase.initializeDatabase();

        // users数据库
        CustomerDatabase customerDatabase=new CustomerDatabase();
        customerDatabase.initializeDatabase();

        
        CustomerRegister customerRegister=new CustomerRegister();
        CustomerLogin customerLogin=new CustomerLogin();
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                displayMenu();

                int choice = getUserChoice(scanner);

                switch (choice) {
                    case 1:
                        handleAdminLogin(scanner, admin);
                        break;
                    case 2:
                        customerRegister.run();
                        break;
                    case 3:
                        customerLogin.run();
                        break;
                    case 4:
                        System.out.println("退出系统");
                        return; // 退出主循环
                    default:
                        System.out.println("无效的选择，请重新输入！");
                }
            }
        } finally {
            scanner.close(); // 确保 Scanner 被关闭
        }
    }

    private static void displayMenu() {
        System.out.println("请选择操作：");
        System.out.println("1. 管理员登录");
        System.out.println("2. 用户注册");
        System.out.println("3. 用户登录");
        System.out.println("4. 退出");
    }

    private static int getUserChoice(Scanner scanner) {
        while (true) {
            try {
                System.out.print("请输入选项：");
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("无效的输入，请输入一个数字！");
                scanner.next(); // 清除错误的输入
            }
        }
    }

    private static void handleAdminLogin(Scanner scanner, Administrator admin) {
        if (admin.loginAdmin(scanner)) {
            System.out.println("管理员登录成功！");
            AdministratorAction adminAction = new AdministratorAction();
            adminAction.run();
        } else {
            System.out.println("管理员登录失败！");
        }
    }
} 

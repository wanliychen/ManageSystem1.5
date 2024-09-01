package org.example;

import java.util.Scanner;

public class CustomerAction implements Actionable {
    private Scanner scanner;

    public CustomerAction() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displayMenu() {
        System.out.println("请选择操作：");
        System.out.println("1. 购物管理");
        System.out.println("2. 修改密码");
        System.out.println("3. 退出登录");
    }

    @Override
    public void run() {
        RunShopping runShopping = new RunShopping();
        CustomerPasswordManage cpm = new CustomerPasswordManage();

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    runShopping.run();
                    break;
                case 2:
                    cpm.changePassword();
                    break;
                case 3:
                    System.out.println("退出用户系统");
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }
}

package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("欢迎使用购物管理系统");
        // admins数据库
        Administrator admin = new Administrator();
        admin.createTable();
        admin.insertDefaultAdmin();
        
        // products数据库
        ProductDatabase productDatabase=new ProductDatabase();
        productDatabase.initializeDatabase();

        // users数据库
        CustomerDatabase customerDatabase=new CustomerDatabase();
        customerDatabase.initializeDatabase();

        
      
    }
} 

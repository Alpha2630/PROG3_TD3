package com.alpha.prog3_td2.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class databaseConnection {
    private final String url;
    private final String password;
    private final String user;

    public databaseConnection(String url, String password, String user) {
        this.url = url;
        this.password = password;
        this.user = user;
        loadDriver();
    }
    public databaseConnection(){
        this.url = "jdbc:postgresql://localhost:5432/product_management_db";
        this.user = "mini_football_db_manager";
        this.password = "123456";
        loadDriver();
    }
    private void loadDriver(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            throw new RuntimeException("Driver postgres not found");
        }
    }
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url, password, user);
    }
}

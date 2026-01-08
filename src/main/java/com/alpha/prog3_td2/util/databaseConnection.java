package com.alpha.prog3_td2.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection {
    private final String url;
    private final String user;      // DÉPLACÉ AVANT password
    private final String password;  // DÉPLACÉ APRÈS user

    // CORRECTION: Change l'ordre des paramètres
    public databaseConnection(String url, String user, String password) {  // ORDRE CORRECT
        this.url = url;
        this.user = user;          // CORRECTION: user d'abord
        this.password = password;  // CORRECTION: password après
        loadDriver();
    }

    public databaseConnection() {
        // CORRECTION: Même ordre ici
        this.url = "jdbc:postgresql://localhost:5432/mini_football_db";
        this.user = "mini_football_db_manager";      // user d'abord
        this.password = "123456";                     // password après
        loadDriver();
    }

    private void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver postgres not found");
        }
    }

    public Connection getConnection() throws SQLException {
        // Ici l'ordre est correct: url, user, password
        return DriverManager.getConnection(url, user, password);
    }
}
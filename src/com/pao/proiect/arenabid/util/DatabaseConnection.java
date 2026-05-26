package com.pao.proiect.arenabid.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("resources/db.properties"));

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            if (user == null || user.isBlank()) {
                this.connection = DriverManager.getConnection(url);
            } else {
                this.connection = DriverManager.getConnection(url, user, password);
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not read database properties file.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Could not connect to database.", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                instance = new DatabaseConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not verify database connection.", e);
        }

        return connection;
    }
}
package com.pao.laboratory14.exercise2.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

            try (InputStream inputStream = openPropertiesFile()) {
                properties.load(inputStream);
            }

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            if (user == null || user.isBlank()) {
                connection = DriverManager.getConnection(url);
            } else {
                connection = DriverManager.getConnection(url, user, password);
            }

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Could not connect to database.", e);
        }
    }

    private InputStream openPropertiesFile() throws IOException {
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("db.properties");

        if (inputStream != null) {
            return inputStream;
        }

        return new FileInputStream(
                "src/com/pao/laboratory14/exercise2/resources/db.properties"
        );
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
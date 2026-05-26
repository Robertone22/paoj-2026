package com.pao.proiect.arenabid.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initializeDatabase() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String schemaSql = Files.readString(Path.of("resources/schema.sql"));

            String[] statements = schemaSql.split(";");

            for (String statementText : statements) {
                String trimmedStatement = statementText.trim();

                if (!trimmedStatement.isEmpty()) {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(trimmedStatement);
                    }
                }
            }

            System.out.println("Database schema initialized successfully.");

        } catch (IOException e) {
            throw new RuntimeException("Could not read schema.sql file.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Could not initialize database schema.", e);
        }
    }
}
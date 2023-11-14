package com.weather.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseIntegrationTest {

    private static final String H2_URL = "jdbc:h2:mem:sa;DB_CLOSE_DELAY=-1;";
    private static final String H2_USER = "sa";
    private static final String H2_PASS = "";
    protected static final String CREATE_SQL;
    protected static final String CLEAN_SQL;

    static {
        try {
            CREATE_SQL = Files.readString(Path.of("src/test/resources/createTestDb.sql"));
            CLEAN_SQL = Files.readString(Path.of("src/test/resources/deleteTestDb.sql"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file: ", e);
        }
    }

    @BeforeAll
    static void prepareDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection(H2_URL, H2_USER, H2_PASS)) {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_SQL);
        }
    }

    @BeforeEach
    void cleanDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:sa;DB_CLOSE_DELAY=-1;", "sa", "")) {
            Statement statement = connection.createStatement();
            statement.execute(CLEAN_SQL);
        }
    }
}

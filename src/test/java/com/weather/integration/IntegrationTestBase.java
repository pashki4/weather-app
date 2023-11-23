package com.weather.integration;

import com.weather.model.Location;
import com.weather.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class IntegrationTestBase {

    private static final String H2_URL = "jdbc:h2:mem:sa;DB_CLOSE_DELAY=-1;";
    private static final String H2_USER = "sa";
    private static final String H2_PASS = "";
    protected static final String CREATE_SQL;
    protected static final String CLEAN_SQL;

    static {
        try {
            CREATE_SQL = Files.readString(Path.of("src/test/resources/init_h2.sql"));
            CLEAN_SQL = Files.readString(Path.of("src/test/resources/clean_h2.sql"));
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
        try (Connection connection = DriverManager.getConnection(H2_URL, H2_USER, H2_PASS)) {
            Statement statement = connection.createStatement();
            statement.execute(CLEAN_SQL);
        }
    }

    protected User getUser() {
        return User.builder()
                .login("login")
                .password("userPassword")
                .build();
    }

    protected Location getLocation() {
        return Location.builder()
                .name("London")
                .latitude(new BigDecimal("51.5073219"))
                .longitude(new BigDecimal("-0.1276474"))
                .country("GB")
                .build();
    }
}

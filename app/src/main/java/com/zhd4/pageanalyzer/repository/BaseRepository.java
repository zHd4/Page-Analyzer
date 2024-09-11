package com.zhd4.pageanalyzer.repository;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseRepository {
    private static HikariDataSource hikariDataSource;

    public static void setDataSource(HikariDataSource dataSource) {
        hikariDataSource = dataSource;
    }

    public static HikariDataSource getDataSource() {
        return hikariDataSource;
    }

    public static void runScript(String sql) throws SQLException {
        try (Connection connection = hikariDataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}

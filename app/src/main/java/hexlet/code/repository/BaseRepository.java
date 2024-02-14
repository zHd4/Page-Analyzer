package hexlet.code.repository;

import com.zaxxer.hikari.HikariDataSource;

public class BaseRepository {
    private static HikariDataSource hikariDataSource;

    public static void setDataSource(HikariDataSource dataSource) {
        hikariDataSource = dataSource;
    }

    public static HikariDataSource getDataSource() {
        return hikariDataSource;
    }
}

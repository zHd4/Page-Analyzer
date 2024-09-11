package com.zhd4.pageanalyzer.repository;

import com.zhd4.pageanalyzer.model.Url;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlsRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                statement.setString(1, url.getName());
                statement.setTimestamp(2, now);

                statement.executeUpdate();

                ResultSet keys = statement.getGeneratedKeys();

                if (keys.next()) {
                    url.setId(keys.getLong(1));
                    url.setCreatedAt(now);
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        }
    }

    public static Optional<Url> findById(long id) throws SQLException {
        String sql = "SELECT * FROM urls WHERE id=?";

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Url foundUrl = null;

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                foundUrl = new Url(name);

                foundUrl.setId(id);
                foundUrl.setCreatedAt(createdAt);
            }

            return Optional.ofNullable(foundUrl);
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name=?";

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            Url foundUrl = null;

            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                foundUrl = new Url(name);

                foundUrl.setId(id);
                foundUrl.setCreatedAt(createdAt);
            }

            return Optional.ofNullable(foundUrl);
        }
    }

    public static List<Url> getEntities() throws SQLException {
        String sql = "SELECT * FROM urls";

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            List<Url> result = new ArrayList<>();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");

                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                Url url = new Url(name);

                url.setId(id);
                url.setCreatedAt(createdAt);

                result.add(url);
            }

            return result;
        }
    }
}

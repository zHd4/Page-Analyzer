package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlsRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, url.getName());
                statement.setTimestamp(2, url.getCreatedAt());

                statement.executeUpdate();

                ResultSet keys = statement.getGeneratedKeys();

                if (keys.next()) {
                    url.setId(keys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            Url foundUrl = null;

            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                foundUrl = new Url(name, createdAt);
                foundUrl.setId(id);
            }

            return Optional.ofNullable(foundUrl);
        }
    }

    public static List<Url> getEntities() throws SQLException {
        String sql = "SELECT * FROM urls";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            List<Url> result = new ArrayList<>();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");

                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                Url url = new Url(name, createdAt);

                url.setId(id);
                result.add(url);
            }

            return result;
        }
    }
}

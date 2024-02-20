package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UrlsChecksRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        String sql = "INSERT INTO urls_checks (status_code, title, h1, description, url_id, created_at)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, urlCheck.getStatusCode());
                statement.setString(2, urlCheck.getTitle());
                statement.setString(3, urlCheck.getH1());
                statement.setString(4, urlCheck.getDescription());
                statement.setLong(5, urlCheck.getUrlId());
                statement.setTimestamp(6, urlCheck.getCreatedAt());

                statement.executeUpdate();

                ResultSet keys = statement.getGeneratedKeys();

                if (keys.next()) {
                    urlCheck.setId(keys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        }
    }

    public static List<UrlCheck> findByUrlId(long urlId) throws SQLException {
        String sql = "SELECT * FROM urls_checks WHERE url_id=?";

        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, urlId);
            ResultSet resultSet = statement.executeQuery();

            List<UrlCheck> result = new ArrayList<>();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");

                int statusCode = resultSet.getInt("status_code");

                String title = resultSet.getString("title");
                String h1 = resultSet.getString("h1");
                String description = resultSet.getString("description");

                Timestamp createdAt = resultSet.getTimestamp("created_at");
                UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);

                urlCheck.setId(id);
                result.add(urlCheck);
            }

            return result;
        }
    }
}

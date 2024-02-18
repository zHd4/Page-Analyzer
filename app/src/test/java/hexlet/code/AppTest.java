package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlsRepository;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static hexlet.code.util.FormatUtils.formatTimestamp;

public final class AppTest {
    private static final int PORT = 0;
    private static Javalin app;
    private static String baseUrl;

    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        app = App.getApp();
        app.start(PORT);

        baseUrl = "http://localhost:" + app.port();
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @AfterEach
    void afterEach() throws SQLException {
        BaseRepository.runScript("DELETE FROM urls;");
        BaseRepository.runScript("ALTER TABLE urls ALTER COLUMN id RESTART WITH 4;");

        BaseRepository.runScript("""
                INSERT INTO urls (id, name, created_at)
                VALUES (1, 'https://github.com', '2024-02-17 00:40:12.491151');

                INSERT INTO urls (id, name, created_at)
                VALUES (2, 'https://google.com', '2024-02-17 00:41:06.273015');

                INSERT INTO urls (id, name, created_at)
                VALUES (3, 'https://microsoft.com', '2024-02-17 00:41:51.817112');""");

    }

    @Test
    void indexTest() {
        HttpResponse<String> response = Unirest.get(baseUrl).asString();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void urlIndexTest() {
        HttpResponse<String> response = Unirest.get(baseUrl + "/urls").asString();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testAdding() throws SQLException {
        String url = "https://www.facebook.com:443/login";
        String expected = "https://www.facebook.com:443";

        Unirest.post(baseUrl + "/urls").field("url", url).asString();
        Optional<Url> actual = UrlsRepository.findByName(expected);

        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.get().getName()).isEqualTo(expected);

        HttpResponse<String> response = Unirest.get(baseUrl + "/urls").asString();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(expected);
    }

    @Test
    void testShowing() throws SQLException {
        long id = 1;
        Optional<Url> optionalUrl = UrlsRepository.findById(id);

        HttpResponse<String> response = Unirest.get(baseUrl + "/urls/" + id).asString();
        String body = response.getBody().replace("\n", "");

        assertThat(optionalUrl.isEmpty()).isFalse();
        Url url = optionalUrl.get();

        assertThat(body).contains(String.format("<td>%s</td>", url.getId()));
        assertThat(body).contains(String.format("<td>%s</td>", url.getName()));
        assertThat(body).contains(String.format("<td>%s</td>", formatTimestamp(url.getCreatedAt())));
    }
}

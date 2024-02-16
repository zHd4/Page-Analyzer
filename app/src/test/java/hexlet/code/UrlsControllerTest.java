package hexlet.code;

import hexlet.code.util.Resources;
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

import static org.assertj.core.api.Assertions.assertThat;
import static hexlet.code.repository.BaseRepository.runScript;

public class UrlsControllerTest {
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
    void afterEach() throws IOException, SQLException {
        runScript(getSqlScript("truncate.sql"));
        runScript(getSqlScript("seed.sql"));
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
    void testAdding() {

    }

    @Test
    void testShowing() {

    }

    private static String getSqlScript(String resourceName) throws IOException {
        return Resources.readResourceFile(resourceName);
    }
}

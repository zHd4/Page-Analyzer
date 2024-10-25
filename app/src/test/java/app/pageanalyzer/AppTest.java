package app.pageanalyzer;

import app.pageanalyzer.model.Url;
import app.pageanalyzer.model.UrlCheck;
import app.pageanalyzer.repository.UrlsRepository;
import app.pageanalyzer.util.FormatUtils;
import app.pageanalyzer.util.NamedRoutes;
import app.pageanalyzer.util.Resources;
import app.pageanalyzer.repository.UrlChecksRepository;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static app.pageanalyzer.repository.BaseRepository.runScript;

public final class AppTest {
    private static final int PORT = 0;
    private static Javalin app;
    private static String baseUrl;
    private static MockWebServer mockServer;

    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        app = App.getApp();
        app.start(PORT);

        baseUrl = "http://localhost:" + app.port();
        mockServer = new MockWebServer();

        String testBody = Resources.readResourceFile("fixtures/index.html");
        MockResponse mockResponse = new MockResponse().setBody(testBody);

        mockServer.enqueue(mockResponse);
        mockServer.start();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        app.stop();
        mockServer.shutdown();
    }

    @AfterEach
    void afterEach() throws SQLException, IOException {
        runScript(Resources.readResourceFile("truncate.sql"));
        runScript(Resources.readResourceFile("seed.sql"));
    }

    @Test
    void indexTest() {
        HttpResponse<String> response = Unirest.get(baseUrl + NamedRoutes.rootPath()).asString();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void urlIndexTest() {
        HttpResponse<String> response = Unirest.get(baseUrl + NamedRoutes.urlsPath()).asString();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testAdding() throws SQLException {
        String url = "https://www.facebook.com:443/login";
        String expected = "https://www.facebook.com:443";

        Unirest.post(baseUrl + "/urls").field("url", url).asEmpty();
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
        assertThat(body).contains(String.format("<td>%s</td>", FormatUtils.formatTimestamp(url.getCreatedAt())));
    }

    @Test
    void testChecking() throws SQLException {
        String url = mockServer.url("/").toString().replaceAll("/$", "");
        Unirest.post(baseUrl + NamedRoutes.urlsPath()).field("url", url).asEmpty();

        Optional<Url> actualOptionalUrl = UrlsRepository.findByName(url);

        assertThat(actualOptionalUrl.isEmpty()).isFalse();
        assertThat(actualOptionalUrl.get().getName()).isEqualTo(url);

        Url actualUrl = actualOptionalUrl.get();
        Unirest.post(baseUrl + NamedRoutes.urlChecksPath(actualUrl.getId())).asEmpty();

        List<UrlCheck> actualChecks = UrlChecksRepository.findByUrlId(actualUrl.getId());
        assertThat(actualChecks.isEmpty()).isFalse();

        UrlCheck actualCheck = actualChecks.get(0);

        assertThat(actualCheck.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualCheck.getTitle()).isEqualTo("Test title");
        assertThat(actualCheck.getH1()).isEqualTo("test h1");
        assertThat(actualCheck.getDescription()).isEqualTo("test description");
    }
}

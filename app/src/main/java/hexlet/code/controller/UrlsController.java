package hexlet.code.controller;

import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlsRepository.getEntities();
        UrlsPage page = new UrlsPage(urls);

        page.setFlashText(ctx.consumeSessionAttribute("flash-text"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        ctx.render("urls.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        String url = ctx.formParam("url");

        try {
            URI uri = new URI(Objects.requireNonNull(url));
            url = String.format("%s://%s", uri.getScheme(), uri.getAuthority());

            if (UrlsRepository.findByName(url).isEmpty()) {
                UrlsRepository.save(new Url(url, Timestamp.from(Instant.now())));

                ctx.sessionAttribute("flash-text", "Страница успешно добавлена");
                ctx.sessionAttribute("flash-type", "success");
            } else {
                ctx.sessionAttribute("flash-text", "Страница уже существует");
                ctx.sessionAttribute("flash-type", "primary");
            }
        } catch (URISyntaxException | NullPointerException e) {
            ctx.sessionAttribute("flash-text", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
        }

        ctx.redirect(NamedRoutes.urlsPath());
    }
}

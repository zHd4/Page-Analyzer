package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlsChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UrlUtils;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlsRepository.getEntities();
        UrlsPage page = new UrlsPage(urls);

        page.setFlashText(ctx.consumeSessionAttribute("flash-text"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        try {
            String url = UrlUtils.parseUrl(ctx.formParam("url"));

            if (UrlsRepository.findByName(url).isEmpty()) {
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                UrlsRepository.save(new Url(url, now));

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

    public static void show(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        Optional<Url> optionalUrl = UrlsRepository.findById(id);

        if (optionalUrl.isEmpty()) {
            throw new NotFoundResponse("Страница с id=" + id +" не найдена");
        }

        Url url = optionalUrl.get();

        List<UrlCheck> urlChecks = UrlsChecksRepository.findByUrlId(url.getId());
        url.setChecks(urlChecks);

        UrlPage page = new UrlPage(url);
        ctx.render("urls/url.jte", Collections.singletonMap("page", page));
    }

    public static void createUrlCheck(Context ctx) throws SQLException {
        long urlId = ctx.pathParamAsClass("id", Long.class).get();
        Optional<Url> optionalUrl = UrlsRepository.findById(urlId);

        if (optionalUrl.isEmpty()) {
            throw new NotFoundResponse("Страница с id=" + urlId +" не найдена");
        }

        Url url = optionalUrl.get();
        UrlCheck urlCheck = UrlUtils.checkUrl(url);

        UrlsChecksRepository.save(urlCheck);
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}

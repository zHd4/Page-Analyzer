package com.zhd4.pageanalyzer.controller;

import com.zhd4.pageanalyzer.model.Url;
import com.zhd4.pageanalyzer.model.UrlCheck;
import com.zhd4.pageanalyzer.util.NamedRoutes;
import com.zhd4.pageanalyzer.dto.urls.UrlPage;
import com.zhd4.pageanalyzer.dto.urls.UrlsPage;
import com.zhd4.pageanalyzer.repository.UrlChecksRepository;
import com.zhd4.pageanalyzer.repository.UrlsRepository;
import com.zhd4.pageanalyzer.util.UrlUtils;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.UnirestException;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlsRepository.getEntities();

        Map<Long, UrlCheck> latestChecks = UrlChecksRepository.getLatest().stream()
                .collect(Collectors.toMap(UrlCheck::getUrlId, urlCheck -> urlCheck));

        UrlsPage page = new UrlsPage(urls, latestChecks);

        page.setFlashText(ctx.consumeSessionAttribute("flash-text"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        try {
            String url = UrlUtils.parseUrl(ctx.formParam("url"));

            if (UrlsRepository.findByName(url).isEmpty()) {
                UrlsRepository.save(new Url(url));

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
            throw new NotFoundResponse("Страница с id=" + id + " не найдена");
        }

        Url url = optionalUrl.get();

        List<UrlCheck> urlChecks = UrlChecksRepository.findByUrlId(url.getId());
        url.setChecks(urlChecks);

        UrlPage page = new UrlPage(url, urlChecks);

        page.setFlashText(ctx.consumeSessionAttribute("flash-text"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        ctx.render("urls/url.jte", Collections.singletonMap("page", page));
    }

    public static void createUrlCheck(Context ctx) throws SQLException {
        long urlId = ctx.pathParamAsClass("id", Long.class).get();
        Optional<Url> optionalUrl = UrlsRepository.findById(urlId);

        if (optionalUrl.isEmpty()) {
            throw new NotFoundResponse("Страница с id=" + urlId + " не найдена");
        }

        Url url = optionalUrl.get();

        try {
            UrlCheck urlCheck = UrlUtils.checkUrl(url);
            UrlChecksRepository.save(urlCheck);

            ctx.sessionAttribute("flash-text", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash-text", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
        }

        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}

package com.zhd4.pageanalyzer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zhd4.pageanalyzer.controller.RootController;
import com.zhd4.pageanalyzer.controller.UrlsController;
import com.zhd4.pageanalyzer.util.NamedRoutes;
import com.zhd4.pageanalyzer.util.Resources;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import com.zhd4.pageanalyzer.repository.BaseRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public class App {
    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);

        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "3000");
        return Integer.parseInt(port);
    }

    private static String getMode() {
        return System.getenv().getOrDefault("APP_ENV", "development");
    }

    private static HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(System.getenv().getOrDefault("JDBC_DATABASE_URL",
                "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;"));

        if (System.getenv().containsKey("PG_USER") && System.getenv().containsKey("PG_PASSWORD")) {
            config.setDriverClassName("org.postgresql.Driver");

            config.setUsername(System.getenv().get("PG_USER"));
            config.setPassword(System.getenv().get("PG_PASSWORD"));
        }

        return config;
    }

    private static boolean isProduction() {
        return getMode().equals("production");
    }

    private static void addRoutes(Javalin app) {
        app.get(NamedRoutes.rootPath(), RootController::index);
        app.get(NamedRoutes.urlsPath(), UrlsController::index);
        app.post(NamedRoutes.urlsPath(), UrlsController::create);
        app.get(NamedRoutes.urlPath(), UrlsController::show);
        app.post(NamedRoutes.urlChecksPath(), UrlsController::createUrlCheck);
    }

    public static Javalin getApp() throws IOException, SQLException {
        HikariConfig hikariConfig = getHikariConfig();
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        String sql = Resources.readResourceFile("schema.sql");

        BaseRepository.setDataSource(dataSource);
        BaseRepository.runScript(sql);

        Javalin app = Javalin.create(config -> {
            if (!isProduction()) {
                config.bundledPlugins.enableDevLogging();
            }

            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        addRoutes(app);

        app.before(ctx -> ctx.attribute("ctx", ctx));
        return app;
    }

    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start(getPort());
    }
}

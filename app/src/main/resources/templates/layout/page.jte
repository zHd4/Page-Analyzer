@import gg.jte.Content
@import app.pageanalyzer.util.NamedRoutes

@param Content content

<html lang="ru">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
              crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
                crossorigin="anonymous"></script>
        <title>Анализатор страниц</title>
    </head>
    <body class="d-flex flex-column min-vh-100">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="/">Анализатор страниц</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <div class="navbar-nav">
                        <a class="nav-link" href="${NamedRoutes.rootPath()}">Главная</a>
                        <a class="nav-link" href="${NamedRoutes.urlsPath()}">Сайты</a>
                    </div>
                </div>
            </div>
        </nav>

        <main class="flex-grow-1">
            <section>
                ${content}
            </section>
        </main>

        <footer class="footer border-top py-3 mt-5 bg-light">
            <div class="container-xl">
                <div class="text-center">
                    Created by
                    <a href="https://github.com/zHd4" target="_blank">zHd4</a>
                </div>
            </div>
        </footer>
    </body>
</html>
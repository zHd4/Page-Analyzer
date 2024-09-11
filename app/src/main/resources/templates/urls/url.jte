@import static util.com.zhd4.pageanalyzer.FormatUtils.formatTimestamp
@import urls.dto.com.zhd4.pageanalyzer.UrlPage
@import util.com.zhd4.pageanalyzer.NamedRoutes
@import model.com.zhd4.pageanalyzer.UrlCheck
@param UrlPage page

@template.layout.page(
content = @`
    @if(page != null && page.getFlashText() != null)
        <div class="alert alert-${page.getFlashType()}" role="alert">
            ${page.getFlashText()}
        </div>
    @endif

    <div class="container-lg mt-5">
        <h1>${page.getUrl().getName()}</h1>

        <table class="table table-bordered table-hover mt-3">
            <tbody>
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Имя</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>Дата добавления</td>
                <td>${formatTimestamp(page.getUrl().getCreatedAt())}</td>
            </tr>
            </tbody>
        </table>

        <h2 class="mt-5">Проверки</h2>
        <form method="post" action="${NamedRoutes.urlChecksPath(page.getUrl().getId())}">
            <button type="submit" class="btn btn-primary">Запустить проверку</button>
        </form>

        <table class="table table-bordered table-hover mt-3">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Код ответа</th>
                    <th>title</th>
                    <th>h1</th>
                    <th>description</th>
                    <th>Дата проверки</th>
                </tr>
            </thead>
            <tbody>
            @if(!page.getChecks().isEmpty())
                @for(UrlCheck check : page.getChecks())
                    <tr>
                        <td>${check.getId()}</td>
                        <td>${check.getStatusCode()}</td>
                        <td>${check.getTitle()}</td>
                        <td>${check.getH1()}</td>
                        <td>${check.getDescription()}</td>
                        <td>${formatTimestamp(check.getCreatedAt())}</td>
                    </tr>
                @endfor
            @endif
            </tbody>
        </table>
    </div>
`
)
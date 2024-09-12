@import static com.zhd4.pageanalyzer.util.FormatUtils.formatTimestamp
@import com.zhd4.pageanalyzer.urls.dto.UrlsPage
@import com.zhd4.pageanalyzer.model.Url
@import com.zhd4.pageanalyzer.util.NamedRoutes
@param UrlsPage page

@template.layout.page(
content = @`
    @if(page != null && page.getFlashText() != null)
        <div class="alert alert-${page.getFlashType()}" role="alert">
            ${page.getFlashText()}
        </div>
    @endif

    <div class="container-lg mt-5">
        <h1>Сайты</h1>

        <table class="table table-bordered table-hover mt-3">
            <thead>
            <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Дата добавления</th>
                <th>Последняя проверка</th>
                <th>Код ответа</th>
            </tr>
            </thead>
            <tbody>
            @for(Url url : page.getUrls())
                <tr>
                    <td>${url.getId()}</td>
                    <td>
                        <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                    </td>
                    <td>${formatTimestamp(url.getCreatedAt())}</td>

                    <td>
                        ${page.getLatestChecks().containsKey(url.getId()) ?
                            formatTimestamp(page.getLatestChecks().get(url.getId()).getCreatedAt()) : ""}
                    </td>

                    <td>
                        ${page.getLatestChecks().containsKey(url.getId()) ?
                            String.valueOf(page.getLatestChecks().get(url.getId()).getStatusCode()) : ""}
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)
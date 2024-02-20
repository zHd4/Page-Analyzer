@import static hexlet.code.util.FormatUtils.formatTimestamp
@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.model.Url
@import hexlet.code.util.NamedRoutes
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
                <th class="col-1">ID</th>
                <th>Имя</th>
                <th class="col-2">Дата добавления</th>
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
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)
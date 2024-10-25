package app.pageanalyzer.dto.urls;

import app.pageanalyzer.dto.BasePage;
import app.pageanalyzer.model.Url;
import app.pageanalyzer.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class UrlsPage extends BasePage {
    private List<Url> urls;
    private Map<Long, UrlCheck> latestChecks;
}

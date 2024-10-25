package app.pageanalyzer.dto.urls;

import app.pageanalyzer.dto.BasePage;
import app.pageanalyzer.model.Url;
import app.pageanalyzer.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UrlPage extends BasePage {
    private Url url;
    private List<UrlCheck> checks;
}

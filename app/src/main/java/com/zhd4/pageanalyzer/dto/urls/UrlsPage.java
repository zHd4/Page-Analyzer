package com.zhd4.pageanalyzer.dto.urls;

import com.zhd4.pageanalyzer.dto.BasePage;
import com.zhd4.pageanalyzer.model.Url;
import com.zhd4.pageanalyzer.model.UrlCheck;
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

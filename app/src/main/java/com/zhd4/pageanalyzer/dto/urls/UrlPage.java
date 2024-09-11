package com.zhd4.pageanalyzer.dto.urls;

import com.zhd4.pageanalyzer.dto.BasePage;
import com.zhd4.pageanalyzer.model.Url;
import com.zhd4.pageanalyzer.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UrlPage extends BasePage {
    private Url url;
    private List<UrlCheck> checks;
}

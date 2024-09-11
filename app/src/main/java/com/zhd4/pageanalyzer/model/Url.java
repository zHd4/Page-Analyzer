package com.zhd4.pageanalyzer.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class Url {
    @Setter
    private Long id;
    private final String name;

    @Setter
    private Timestamp createdAt;

    @Setter
    private List<UrlCheck> checks;

    public Url(String name) {
        this.name = name;
    }
}

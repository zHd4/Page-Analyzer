package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
public class UrlCheck {
    @Setter
    private long id;

    private final int statusCode;
    private final String title;
    private final String h1;
    private final String description;
    private final long urlId;
    private final Timestamp createdAt;

    public UrlCheck(int statusCode, String title, String h1, String description, long urlId, Timestamp createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
        this.createdAt = createdAt;
    }
}

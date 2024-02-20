package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class Url {
    @Setter
    private Long id;

    private final String name;
    private final Timestamp createdAt;

    @Setter
    private List<UrlCheck> checks;

    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
}

package hexlet.code.util;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public final class UrlUtils {
    public static String parseUrl(String url) throws URISyntaxException {
        URI uri = new URI(Objects.requireNonNull(url));

        String protocol = uri.getScheme();
        String authority = uri.getAuthority();

        if (protocol == null
                || authority == null
                || (!protocol.equals("http") && !protocol.equals("https"))) {
            throw new URISyntaxException(url, "Incorrect URL");
        }

        return String.format("%s://%s", protocol, authority);
    }

    public static UrlCheck checkUrl(Url url) {
        HttpResponse<String> response = Unirest.get(url.getName()).asString();
        Document document = Jsoup.parse(response.getBody());

        int status = response.getStatus();
        String title = document.title();

        Element h1Element = document.selectFirst("h1");
        String h1 = h1Element != null ? h1Element.text() : "";

        Element descriptionElement = document.selectFirst("meta[name=description]");
        String description = descriptionElement != null ? descriptionElement.attr("content") : "";

        return new UrlCheck(status, title, h1, description, url.getId());
    }
}

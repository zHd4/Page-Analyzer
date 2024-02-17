package hexlet.code.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public final class UrlUtils {
    public static String parseUrl(String url) throws URISyntaxException {
        URI uri = new URI(Objects.requireNonNull(url));

        String protocol = uri.getScheme();
        String authority = uri.getAuthority();

        if (protocol == null || authority == null) {
            throw new URISyntaxException(url, "Incorrect URL");
        }

        return String.format("%s://%s", protocol, authority);
    }
}

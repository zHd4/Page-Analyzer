package app.pageanalyzer.util;

import app.pageanalyzer.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Resources {
    public static String readResourceFile(String fileName) throws IOException {
        InputStream inputStream = Objects.requireNonNull(App.class.getClassLoader().getResourceAsStream(fileName));
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        try (BufferedReader reader = new BufferedReader(streamReader)) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}

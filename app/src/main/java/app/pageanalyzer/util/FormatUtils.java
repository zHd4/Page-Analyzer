package app.pageanalyzer.util;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public final class FormatUtils {
    public static String formatTimestamp(Timestamp timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        return formatter.format(timestamp.toLocalDateTime());
    }
}

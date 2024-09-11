package com.zhd4.pageanalyzer.util;

public final class NamedRoutes {
    public static String rootPath() {
        return "/";
    }

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlPath() {
        return urlPath("{id}");
    }

    public static String urlPath(long id) {
        return urlPath(String.valueOf(id));
    }

    public static String urlPath(String id) {
        return "/urls/" + id;
    }

    public static String urlChecksPath() {
        return urlChecksPath("{id}");
    }

    public static String urlChecksPath(long id) {
        return urlChecksPath(String.valueOf(id));
    }

    public static String urlChecksPath(String id) {
        return "/urls/" + id + "/checks";
    }
}

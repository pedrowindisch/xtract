package br.com.windisch.xtract.compiler.utils;

public final class Validators {
    private static final String URL_REGEX =
        "^(?:(https?|ftp)://)?[\\w.-]+(?:\\.[\\w.-]+)+[/\\w._-]*$";

    public static boolean isValidUrl(String url) {
        return url != null && url.matches(URL_REGEX);
    }
}

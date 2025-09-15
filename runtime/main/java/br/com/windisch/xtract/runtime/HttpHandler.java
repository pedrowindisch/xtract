package br.com.windisch.xtract.runtime;

import org.jsoup.helper.HttpConnection.Response;

import okhttp3.OkHttpClient;

public static class HttpHandler {
    public static HtmlHandler get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new XtractRuntimeException("Unexpected code " + response);

            return response.body();
        }
    }
}

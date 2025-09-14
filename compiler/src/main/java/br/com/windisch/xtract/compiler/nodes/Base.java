package br.com.windisch.xtract.compiler.nodes;

public final class Base implements Node {
    private String url;

    public Base(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

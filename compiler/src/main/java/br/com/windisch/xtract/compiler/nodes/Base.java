package br.com.windisch.xtract.compiler.nodes;

public class Base extends Node {
    private String url;

    public Base(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

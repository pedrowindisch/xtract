package br.com.windisch.xtract.compiler.nodes;

import java.util.List;

import br.com.windisch.xtract.compiler.models.Method;

public final class Scrape implements Node {
    private String name;
    private Method method;
    private String url;

    private List<Parser> parsers;

    public Scrape(String name, Method method, String url, List<Parser> parsers) {
        this.name = name;
        this.method = method;
        this.url = url;
        this.parsers = parsers;
    }

    public String getName() {
        return name;
    }

    public Method getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public List<Parser> getParsers() {
        return parsers;
    }
}

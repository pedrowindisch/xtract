package br.com.windisch.xtract.compiler.nodes;

import java.util.List;

public final class Program implements Node {
    private Base base;
    private List<Scrape> scrapes;

    public Program(Base base, List<Scrape> scrapes) {
        this.base = base;
        this.scrapes = scrapes;
    }

    public Base getBase() {
        return base;
    }

    public List<Scrape> getScrapes() {
        return scrapes;
    }
}

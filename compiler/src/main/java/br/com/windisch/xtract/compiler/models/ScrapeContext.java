package br.com.windisch.xtract.compiler.models;

import java.util.List;

public record ScrapeContext(
    String name,
    String url,
    List<ParseContext> parseContexts
) {}

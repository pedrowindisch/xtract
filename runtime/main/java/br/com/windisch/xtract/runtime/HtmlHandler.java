package br.com.windisch.xtract.runtime;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public static class HtmlHandler {
    public static Element findElement(Document doc, String cssSelector) {
        return doc.select(cssSelector);
    }

    public static Element findElement(Element parent, String cssSelector) {
        return parent.select(cssSelector);
    }
}

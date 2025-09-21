package br.com.windisch.xtract.compiler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import br.com.windisch.xtract.compiler.models.InterpreterException;
import br.com.windisch.xtract.compiler.models.Method;
import br.com.windisch.xtract.compiler.nodes.Base;
import br.com.windisch.xtract.compiler.nodes.ParseBody;
import br.com.windisch.xtract.compiler.nodes.Program;
import br.com.windisch.xtract.compiler.nodes.Scrape;
import br.com.windisch.xtract.compiler.utils.Validators;
import netscape.javascript.JSException;
import br.com.windisch.xtract.compiler.nodes.Parser;

public class Interpreter {
    private final HttpClient httpClient;
    private String baseUrl;
    private Document html;

    public Interpreter() {
        this.httpClient = HttpClient.newHttpClient();
    }
    
    public void eval(Program program) throws InterpreterException {
        if (program.getBase() != null) evalBase(program.getBase());
        for (Scrape scrape : program.getScrapes()) evalScrape(scrape);
    }
    
    public void evalBase(Base base) {
        this.baseUrl = base.getUrl();
    }

    public void evalScrape(Scrape scrape) throws InterpreterException {
        String url;
        if (baseUrl != null) url = baseUrl + scrape.getUrl();
        else url = scrape.getUrl();
       
        if (!Validators.isValidUrl(url)) {
            System.err.println("Invalid URL: " + url);
            return;
        }

        URI uri = URI.create(url);
        if (uri.getScheme() == null || uri.getHost() == null) {
            System.out.println("No protocol or host specified in URL: " + url);
            System.out.println("Assuming http:// as default protocol.");

            uri = URI.create("http://" + url);
        }

        var request = HttpRequest.newBuilder(uri);
        switch (scrape.getMethod()) {
            case GET -> request.GET();
            case POST -> request.POST(HttpRequest.BodyPublishers.noBody());
        }

        try {
            var response = httpClient.send(request.build(), java.net.http.HttpResponse.BodyHandlers.ofString());
            System.out.println("Response status code: " + response.statusCode());

            html = Jsoup.parse(response.body());
        } catch (Exception e) {
            System.err.println("Error during HTTP request: " + e.getMessage());
            return;
        }

        for (var parsers : scrape.getParsers()) evalParser(parsers);
    }

    private void evalParser(Parser parser) throws InterpreterException {
        evalParseBody(parser.getParseBody());
        evalOutput(parser.getOutput());
    }

    private void evalParseBody(ParseBody body) throws InterpreterException {
        var node = html.select(body.getSelector());
        if (node.isEmpty()) {
            System.out.println("No elements found for selector: " + body.getSelector());
            return;
        }

        if (node.size() > 1)
            throw new InterpreterException("Multiple elements found for selector: " + body.getSelector());

        var element = node.first();
        for (var field : body.getSelectFields()) {
            switch (field.getExtractionMethodType()) {
                case TEXT -> System.out.println(element.text());
                case ATTR -> {
                    var attrValue = element.attr(field.getAttributeName());
                    if (attrValue.isEmpty())
                        System.out.println("No attribute '" + field.getAttributeName() + "' found in element.");
                    else
                        System.out.println(attrValue);
                }
            }
        }
    }

    private void evalOutput(br.com.windisch.xtract.compiler.nodes.Output output) {
        System.out.println("Output file name: " + output.getFileName());
        System.out.println("Output format: " + output.getOutputType());
    }
}

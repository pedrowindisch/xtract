package br.com.windisch.xtract.compiler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import br.com.windisch.xtract.compiler.models.InterpreterException;
import br.com.windisch.xtract.compiler.models.ParseContext;
import br.com.windisch.xtract.compiler.models.ScrapeContext;
import br.com.windisch.xtract.compiler.models.XtractCompilerException;
import br.com.windisch.xtract.compiler.nodes.Base;
import br.com.windisch.xtract.compiler.nodes.Output;
import br.com.windisch.xtract.compiler.nodes.ParseBody;
import br.com.windisch.xtract.compiler.nodes.Program;
import br.com.windisch.xtract.compiler.nodes.Scrape;
import br.com.windisch.xtract.compiler.utils.Validators;
import br.com.windisch.xtract.compiler.nodes.Parser;

public class Interpreter {
    private final HttpClient httpClient;
    private String baseUrl;
    private Document html;

    private final List<ScrapeContext> scrapeContexts;
    
    private ScrapeContext currentScrapeContext;
    private ParseContext currentParseContext;

    public Interpreter() {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = null;
        this.html = null;
        this.scrapeContexts = new ArrayList<>();
    }
    
    public void eval(Program program) throws InterpreterException, XtractCompilerException {
        if (program.getBase() != null) evalBase(program.getBase());
        for (Scrape scrape : program.getScrapes()) evalScrape(scrape);
    }

    public List<ScrapeContext> getScrapeContexts() {
        return scrapeContexts;
    }
    
    public void evalBase(Base base) {
        this.baseUrl = base.getUrl();
    }

    public void evalScrape(Scrape scrape) throws InterpreterException, XtractCompilerException {
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

        addScrapeContext(scrape);

        for (var parsers : scrape.getParsers()) evalParser(parsers);
    }

    private void addScrapeContext(Scrape scrape) {
        var context = new ScrapeContext(scrape.getName(), scrape.getUrl(), new ArrayList<>());
        this.scrapeContexts.add(context);
        this.currentScrapeContext = context;
    }

    private void addParsedData(String fieldName, String value) throws XtractCompilerException {
        if (currentScrapeContext == null) throw new XtractCompilerException("No current scrape context.");
        if (currentParseContext == null) throw new XtractCompilerException("No current parse context.");
        
        // @todo: this is actually supposed to be handled in the semantic analysis phase.
        if (currentParseContext.getData().containsKey(fieldName))
            throw new XtractCompilerException("Field '" + fieldName + "' already exists in the current scrape context.");
    
        currentParseContext.getData().put(fieldName, value);
    }

    private void evalParser(Parser parser) throws XtractCompilerException {
        evalParseBody(parser.getParseBody());
        evalOutput(parser.getOutput());
    }

    private void evalParseBody(ParseBody body) throws InterpreterException, XtractCompilerException {
        var node = html.select(body.getSelector());
        if (node.isEmpty()) {
            System.out.println("No elements found for selector: " + body.getSelector());
            return;
        }

        currentParseContext = new ParseContext();
        currentScrapeContext.parseContexts().add(currentParseContext);

        if (node.size() > 1)
            throw new InterpreterException("Multiple elements found for selector: " + body.getSelector());

        var element = node.first();
        for (var field : body.getSelectFields()) {
            switch (field.getExtractionMethodType()) {
                case TEXT -> addParsedData(field.getField(), element.text());
                case ATTR -> {
                    var attrValue = element.attr(field.getAttributeName());

                    if ((attrValue == null || attrValue.isEmpty()) && !field.isOptional())
                        throw new InterpreterException("Attribute '" + field.getAttributeName() + "' not found in element for field '" + field.getField() + "'.");

                    addParsedData(field.getField(), attrValue);
                }
            }
        }
    }

    private void evalOutput(Output output) {
        System.out.println("Output file name: " + output.getFileName());
        System.out.println("Output format: " + output.getOutputType());

        currentParseContext.setOutputFileName(output.getFileName());
    }
}

package br.com.windisch.xtract.compiler;

import java.util.ArrayList;
import java.util.List;

import br.com.windisch.xtract.compiler.models.DocumentType;
import br.com.windisch.xtract.compiler.models.Method;
import br.com.windisch.xtract.compiler.models.ParserException;
import br.com.windisch.xtract.compiler.models.Token;
import br.com.windisch.xtract.compiler.models.TokenType;
import br.com.windisch.xtract.compiler.nodes.Base;
import br.com.windisch.xtract.compiler.nodes.Program;
import br.com.windisch.xtract.compiler.nodes.Scrape;

public class Parser {
    // recursive descent parser

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private boolean isAtEnd() {
        return current >= tokens.size();
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private boolean match(TokenType... types) {
        for (var type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) throws ParserException {
        if (!check(type)) throw new ParserException(peek().getPosition(), message);

        return advance();
    }

    public Program parse() throws ParserException {
        var base = parseBase();
        var scrapes = parseScrapes();

        return new Program(base, scrapes);
    }

    public Base parseBase() throws ParserException {
        if (match(TokenType.BASE)) {
            var url = consume(TokenType.STRING, "Expected URL after 'base'");
            return new Base(url.getLexeme());
        }

        return null;
    }

    public List<Scrape> parseScrapes() throws ParserException {
        List<Scrape> scrapes = new ArrayList<>();
        while (!isAtEnd()) scrapes.add(parseScrape());

        return scrapes;
    }

    public Scrape parseScrape() throws ParserException {
        if (!match(TokenType.SCRAPE)) throw new ParserException(peek().getPosition(), "Expected 'scrape'");

        var identifier = consume(TokenType.IDENTIFIER, "Missing scraper name.");

        consume(TokenType.LBRACE, "Expected '{' after scraper name.");

        if (!check(TokenType.GET)) throw new ParserException(peek().getPosition(), "Missing method in scraper body.");
        
        Method method;
        switch (advance().getType()) {
            case GET:
                method = Method.GET;
                break;
            default:
                throw new ParserException(peek().getPosition(), "Unsupported method.");
        }

        var url = consume(TokenType.STRING, "Missing URL in method.");

        List<br.com.windisch.xtract.compiler.nodes.Parser> parsers = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !isAtEnd()) parsers.add(parseParse());

        consume(TokenType.RBRACE, "Expected '}' after scraper body.");

        return new Scrape(identifier.getLexeme(), method, url.getLexeme(), parsers);
    }

    public List<br.com.windisch.xtract.compiler.nodes.Parser> parseScrapeBody() throws ParserException {
        return null;
    }

    public br.com.windisch.xtract.compiler.nodes.Parser parseParse() throws ParserException {
        if (!match(TokenType.PARSE)) throw new ParserException(peek().getPosition(), "Expected 'parse'");

        if (match(TokenType.HTML)) consume(TokenType.LBRACE, "Expected '{' after 'html'.");

        // var body = parseParseBody();

        while (!check(TokenType.RBRACE) && !isAtEnd()) advance();
        consume(TokenType.RBRACE, "Expected '}' after parser body.");

        while (!check(TokenType.RBRACE) && !isAtEnd()) advance();
        consume(TokenType.RBRACE, "Expected '}' after parser body.");

        if (check(TokenType.OUTPUT)) {
            advance();

            if (!match(TokenType.JSON, TokenType.CSV)) {
                throw new ParserException(peek().getPosition(), "Expected output format (json or csv).");
            }

            if (match(TokenType.APPEND)) {
            }

            var fileName = consume(TokenType.STRING, "Expected output file name.");
        }

        return new br.com.windisch.xtract.compiler.nodes.Parser(DocumentType.HTML, null, null);
    }
}

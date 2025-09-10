package br.com.windisch.xtract.compiler;

import java.util.ArrayList;
import java.util.List;

import br.com.windisch.xtract.compiler.models.DocumentType;
import br.com.windisch.xtract.compiler.models.ExtractionMethodType;
import br.com.windisch.xtract.compiler.models.Method;
import br.com.windisch.xtract.compiler.models.OutputType;
import br.com.windisch.xtract.compiler.models.ParserException;
import br.com.windisch.xtract.compiler.models.Token;
import br.com.windisch.xtract.compiler.models.TokenType;
import br.com.windisch.xtract.compiler.nodes.Base;
import br.com.windisch.xtract.compiler.nodes.Output;
import br.com.windisch.xtract.compiler.nodes.ParseBody;
import br.com.windisch.xtract.compiler.nodes.Program;
import br.com.windisch.xtract.compiler.nodes.Scrape;
import br.com.windisch.xtract.compiler.nodes.SelectField;

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

    private boolean check(TokenType... types) {
        for (var type : types) if (check(type)) return true;

        return false;
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

    public br.com.windisch.xtract.compiler.nodes.Parser parseParse() throws ParserException {
        if (!match(TokenType.PARSE)) throw new ParserException(peek().getPosition(), "Expected 'parse'");

        if (match(TokenType.HTML)) consume(TokenType.LBRACE, "Expected '{' after 'html'.");

        var body = parseParseBody();

        consume(TokenType.RBRACE, "Expected '}' after parser body.");

        Output output = null;
        if (check(TokenType.OUTPUT)) parseOutput();

        return new br.com.windisch.xtract.compiler.nodes.Parser(DocumentType.HTML, body, output);
    }

    public ParseBody parseParseBody() throws ParserException {
        consume(TokenType.SELECT, "Expected 'select' in parser body.");
        var selector = consume(TokenType.STRING, "Expected selector string after 'select'.");

        consume(TokenType.AS, "Expected 'as' after selector.");
        var name = consume(TokenType.IDENTIFIER, "Expected identifier after 'as'.");

        consume(TokenType.LBRACE, "Expected '{' after parser declaration.");

        List<SelectField> fields = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            var fieldName = consume(TokenType.IDENTIFIER, "Expected field name.");
            consume(TokenType.COLON, "Expected ':' after field name.");

            var isOptional = match(TokenType.OPTIONAL);

            if (!check(TokenType.TEXT, TokenType.ATTR)) throw new ParserException(peek().getPosition(), "Expected extraction type (text or attr).");

            var type = advance().getType() == TokenType.TEXT ? ExtractionMethodType.TEXT : ExtractionMethodType.ATTR;

            consume(TokenType.LPAREN, "Expected '(' after extraction type.");

            var attrName = check(TokenType.STRING) ? consume(TokenType.STRING, "Expected attribute or element name for 'attr' extraction type.") : null;

            consume(TokenType.RPAREN, "Expected ')' after extraction method.");

            fields.add(new SelectField(fieldName.getLexeme(), isOptional, type, attrName != null ? attrName.getLexeme() : null));
        }

        while (!check(TokenType.RBRACE) && !isAtEnd()) advance();
        consume(TokenType.RBRACE, "Expected '}' after parser body.");

        return new ParseBody(selector.getLexeme(), name.getLexeme(), fields);
    }

    public Output parseOutput() throws ParserException {
        advance();

        if (!match(TokenType.JSON, TokenType.CSV)) throw new ParserException(peek().getPosition(), "Expected output format (json or csv).");

        var format = previous().getType() == TokenType.JSON ? OutputType.JSON : OutputType.CSV;

        boolean append = match(TokenType.APPEND);
        var fileName = consume(TokenType.STRING, "Expected output file name.");

        return new Output(append, format, fileName.getLexeme());
    }
}

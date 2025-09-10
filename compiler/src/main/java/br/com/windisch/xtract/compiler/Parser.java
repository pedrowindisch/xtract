package br.com.windisch.xtract.compiler;

import java.util.List;

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

    public List<Scrape> parseScrapes() {
        // TODO
        return null;
    }
}

package br.com.windisch.xtract.compiler;

import java.util.ArrayList;
import java.util.List;

import br.com.windisch.xtract.compiler.models.LexerException;
import br.com.windisch.xtract.compiler.models.Position;
import br.com.windisch.xtract.compiler.models.Token;
import br.com.windisch.xtract.compiler.models.TokenType;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();    

    private int start = 0;
    private int current = 0;

    private int line = 1;
    private int column = 1;

    public Lexer(String source) {
        this.source = source;        
    }
    
    public List<Token> run() throws LexerException {
        while (!isAtEnd()) {
            char currentChar = advance();
            switch (currentChar) {
                case ' ':
                case '\r':
                case '\t':
                    // Ignore whitespace
                    break;
                case '\n':
                    line++;
                    column = 1;
                    break;
                case '(':
                    addToken(TokenType.LPAREN);
                    break;
                case ')':
                    addToken(TokenType.RPAREN);
                    break;
                case '{':
                    addToken(TokenType.LBRACE);
                    break;
                case '}':
                    addToken(TokenType.RBRACE);
                    break;
                case ',':
                    addToken(TokenType.COMMA);
                    break;
                case ':':
                    addToken(TokenType.COLON);
                    break;
                case '"':
                    StringBuilder value = new StringBuilder();
    
                    while (!isAtEnd() && source.charAt(current) != '"') {
                        if (source.charAt(current) == '\n')
                            // I chose not to support multi-line strings for now
                            throw new LexerException(new Position(line, column), "Multiline strings are not supported.");
    
                        value.append(currentChar);
                        advance();
                    }
    
                    if (isAtEnd()) throw new LexerException(new Position(line, column), "Unterminated string.");
    
                    advance(); // closing quote
    
                    addToken(TokenType.STRING, value.toString());
                    break;
                default:
                    if (!Character.isAlphabetic(currentChar))
                        throw new LexerException(new Position(line, column), "Unexpected character: " + currentChar);
                    
                    StringBuilder identifier = new StringBuilder();
                    identifier.append(currentChar);
                    while (!isAtEnd() && Character.isAlphabetic(source.charAt(current)))
                        identifier.append(advance());
                    
                    String identStr = identifier.toString();
                    switch (identStr) {
                        case "base":
                            addToken(TokenType.BASE);
                            break;
                        case "scrape":
                            addToken(TokenType.SCRAPE);
                            break;
                        case "get":
                            addToken(TokenType.GET);
                            break;
                        case "call":
                            addToken(TokenType.CALL);
                            break;
                        case "for":
                            addToken(TokenType.FOR);
                            break;
                        case "parse":
                            addToken(TokenType.PARSE);
                            break;
                        case "html":
                            addToken(TokenType.HTML);
                            break;
                        case "select":
                            addToken(TokenType.SELECT);
                            break;
                        case "as":
                            addToken(TokenType.AS);
                            break;
                        case "optional":
                            addToken(TokenType.OPTIONAL);
                            break;
                        case "output":
                            addToken(TokenType.OUTPUT);
                            break;
                        case "append":
                            addToken(TokenType.APPEND);
                            break;
                        case "json":
                            addToken(TokenType.JSON);
                            break;
                        case "csv":
                            addToken(TokenType.CSV);
                            break;
                        case "text":
                            addToken(TokenType.TEXT);
                            break;
                        case "attr":
                            addToken(TokenType.ATTR);
                            break;
                        default:
                            addToken(TokenType.IDENTIFIER, identStr);
                            break;
                    }
    
                    break;
            }
        }
        
        return tokens;
    }

    private void addToken(TokenType type) {
        tokens.add(new Token(type, source.substring(start, current), new Position(line, column)));
    }

    private void addToken(TokenType type, String lexeme) {
        tokens.add(new Token(type, lexeme, new Position(line, column)));
    }

    private char advance() {
        current++;
        column++;

        return source.charAt(current - 1);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
}

package br.com.windisch.xtract.compiler.models;

public class Token {
    private final TokenType type;
    private final String lexeme;
    private final Position position;

    public Token(TokenType type, String lexeme, Position position) {
        this.type = type;
        this.lexeme = lexeme;
        this.position = position;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Position getPosition() {
        return position;
    }
}

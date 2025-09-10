package br.com.windisch.xtract.compiler.models;

public class LexerException extends XtractCompilerException {
    public LexerException(Position position, String message) {
        super(position, "Lexer error at line " + position.getLine() + ", column " + position.getColumn() + ": " + message);
    }
}

package br.com.windisch.xtract.compiler.models;

public class ParserException extends XtractCompilerException {
    public ParserException(Position position, String message) {
        super(position, "Error at " + position + ": " + message);
    }   
}

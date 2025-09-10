package br.com.windisch.xtract.compiler.models;

public class XtractCompilerException extends Exception {
    private Position position;

    public XtractCompilerException(String message) {
        super(message);
    }

    public XtractCompilerException(Position position, String message) {
        super(message);
        
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
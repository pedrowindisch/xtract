package br.com.windisch.xtract.compiler.models;

public class InterpreterException extends XtractCompilerException {
    public InterpreterException(String message) {
        super("Interpreter error: " + message);
    }   
}

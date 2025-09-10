package br.com.windisch.xtract.compiler.formatters;

import br.com.windisch.xtract.compiler.models.XtractCompilerException;

public class ParserExceptionFormatter extends XtractExceptionFormatter {
    public ParserExceptionFormatter(String sourceCode, XtractCompilerException exception) {
        super(sourceCode, exception);
    }

    @Override
    public String format() {
        var fullErrorLine = this.sourceCode.split("\n")[this.exception.getPosition().getLine() - 1];
        var caret = " ".repeat(this.exception.getPosition().getColumn() - 1) + "^";

        return String.format("Parser Error: %s\nAt line %d, column %d\n\n%s\n%s",
                this.exception.getMessage(),
                this.exception.getPosition().getLine(),
                this.exception.getPosition().getColumn(),
                fullErrorLine,
                caret
        );
    }    
}

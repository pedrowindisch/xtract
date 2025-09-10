package br.com.windisch.xtract.compiler.formatters;

import br.com.windisch.xtract.compiler.models.XtractCompilerException;

public abstract class XtractExceptionFormatter {
    protected String sourceCode;
    protected XtractCompilerException exception;

    public XtractExceptionFormatter(String sourceCode, XtractCompilerException exception) {
        this.sourceCode = sourceCode;
        this.exception = exception;
    }

    public abstract String format();
}

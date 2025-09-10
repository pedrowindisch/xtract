package br.com.windisch.xtract.compiler.nodes;

import br.com.windisch.xtract.compiler.models.OutputType;

public class Output extends Node {
    private boolean append;
    private OutputType outputType;
    private String fileName;

    public Output(boolean append, OutputType outputType, String fileName) {
        this.append = append;
        this.outputType = outputType;
        this.fileName = fileName;
    }

    public boolean isAppend() {
        return append;
    }

    public OutputType getOutputType() {
        return outputType;
    }

    public String getFileName() {
        return fileName;
    }
}

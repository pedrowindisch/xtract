package br.com.windisch.xtract.compiler.models;

import java.util.HashMap;
import java.util.Map;

// public class ParseContext(
//     String outputFileName,
//     Map<String, String> data
// ) {}

public class ParseContext {
    private String outputFileName;
    private Map<String, String> data;

    public ParseContext() {
        data = new HashMap<>();
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
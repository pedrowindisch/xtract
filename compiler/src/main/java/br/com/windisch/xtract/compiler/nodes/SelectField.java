package br.com.windisch.xtract.compiler.nodes;

import br.com.windisch.xtract.compiler.models.ExtractionMethodType;

public class SelectField extends Node {
    private String field;
    private ExtractionMethodType extractionMethodType;
    private String attributeName;

    public SelectField(String field, ExtractionMethodType extractionMethodType, String attributeName) {
        this.field = field;
        this.extractionMethodType = extractionMethodType;
        this.attributeName = attributeName;
    }

    public SelectField(String field, ExtractionMethodType extractionMethodType) {
        this(field, extractionMethodType, null);
    }

    public String getField() {
        return field;
    }

    public ExtractionMethodType getExtractionMethodType() {
        return extractionMethodType;
    }

    public String getAttributeName() {
        return attributeName;
    }
}

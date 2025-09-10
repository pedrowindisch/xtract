package br.com.windisch.xtract.compiler.nodes;

import br.com.windisch.xtract.compiler.models.ExtractionMethodType;

public class SelectField extends Node {
    private String field;
    private boolean isOptional;
    private ExtractionMethodType extractionMethodType;
    private String attributeName;

    public SelectField(String field, boolean isOptional, ExtractionMethodType extractionMethodType, String attributeName) {
        this.field = field;
        this.isOptional = isOptional;
        this.extractionMethodType = extractionMethodType;
        this.attributeName = attributeName;
    }

    public SelectField(String field, boolean isOptional, ExtractionMethodType extractionMethodType) {
        this(field, isOptional, extractionMethodType, null);
    }

    public String getField() {
        return field;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public ExtractionMethodType getExtractionMethodType() {
        return extractionMethodType;
    }

    public String getAttributeName() {
        return attributeName;
    }
}

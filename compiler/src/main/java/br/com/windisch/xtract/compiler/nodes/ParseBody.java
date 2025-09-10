package br.com.windisch.xtract.compiler.nodes;

import java.util.List;

public class ParseBody extends Node {
    private String selector;
    private String field;

    private List<SelectField> selectFields;

    public ParseBody(String selector, String field, List<SelectField> selectFields) {
        this.selector = selector;
        this.field = field;
        this.selectFields = selectFields;
    }

    public String getSelector() {
        return selector;
    }

    public String getField() {
        return field;
    }

    public List<SelectField> getSelectFields() {
        return selectFields;
    }
}

package br.com.windisch.xtract.compiler.nodes;

import br.com.windisch.xtract.compiler.models.DocumentType;

public class Parser extends Node {
    private DocumentType documentType;
    private ParseBody parseBody;
    private Output output;

    public Parser(DocumentType documentType, ParseBody parseBody, Output output) {
        this.documentType = documentType;
        this.parseBody = parseBody;
        this.output = output;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public ParseBody getParseBody() {
        return parseBody;
    }

    public Output getOutput() {
        return output;
    }
}

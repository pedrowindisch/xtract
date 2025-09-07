package br.com.windisch.xtract.compiler.models;

public enum TokenType {
    // lexer grammar XtractLexer;

    // // Others
    // LBRACE: '{';
    // RBRACE: '}';
    // LPAREN: '(';
    // RPAREN: ')';
    // COLON: ':';
    // COMMA: ',';
    LBRACE, RBRACE, LPAREN, RPAREN, COLON, COMMA,

    // // Keywords
    // BASE: 'base';
    // SCRAPE: 'scrape';
    // GET: 'get';
    // CALL: 'call';
    // FOR: 'for';
    // PARSE: 'parse';
    // HTML: 'html';
    // SELECT: 'select';
    // AS: 'as';
    // OPTIONAL: 'optional';
    // OUTPUT: 'output';
    // APPEND: 'append';
    // JSON: 'json';
    // CSV: 'csv';
    BASE, SCRAPE, GET, CALL, FOR, PARSE, HTML, SELECT, AS, OPTIONAL, OUTPUT, APPEND, JSON, CSV,

    // // Functions
    // TEXT: 'text';
    // ATTR: 'attr';
    TEXT, ATTR,

    // // Whitespace, newlines and comments
    // WS: [ \t\r\n]+ -> skip;
    // COMMENT: '//' ~[\r\n]* -> skip;
    // ML_COMMENT: '/*' .*? '*/' -> skip;
    WS, COMMENT, ML_COMMENT,

    // // Identifiers and literals
    // IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
    // STRING: '"' (~["\\] | '\\' .)* '"' | '\'' (~['\\] | '\\' .)* '\'';
    // NUMBER: [0-9]+ ('.' [0-9]+)?;
    IDENTIFIER, STRING, NUMBER,
}

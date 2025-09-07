package br.com.windisch.xtract.compiler.models;

public enum TokenType {
    // LBRACE: '{';
    // RBRACE: '}';
    // LPAREN: '(';
    // RPAREN: ')';
    // COLON: ':';
    // COMMA: ',';
    LBRACE, RBRACE, LPAREN, RPAREN, COLON, COMMA,

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

    // TEXT: 'text';
    // ATTR: 'attr';
    TEXT, ATTR,

    // WS: [ \t\r\n]+ -> skip;
    // COMMENT: '//' ~[\r\n]* -> skip;
    // ML_COMMENT: '/*' .*? '*/' -> skip;
    WS, COMMENT, ML_COMMENT,

    // IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
    // STRING: '"' (~["\\] | '\\' .)* '"' | '\'' (~['\\] | '\\' .)* '\'';
    // NUMBER: [0-9]+ ('.' [0-9]+)?;
    IDENTIFIER, STRING, NUMBER,
}

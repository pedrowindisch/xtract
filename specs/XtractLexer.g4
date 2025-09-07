lexer grammar XtractLexer;

// Others
LBRACE: '{';
RBRACE: '}';
LPAREN: '(';
RPAREN: ')';
COLON: ':';
COMMA: ',';

// Keywords
BASE: 'base';
SCRAPE: 'scrape';
GET: 'get';
CALL: 'call';
FOR: 'for';
PARSE: 'parse';
HTML: 'html';
SELECT: 'select';
AS: 'as';
OPTIONAL: 'optional';
OUTPUT: 'output';
APPEND: 'append';
JSON: 'json';
CSV: 'csv';

// Functions
TEXT: 'text';
ATTR: 'attr';

// Whitespace, newlines and comments
WS: [ \t\r\n]+ -> skip;
COMMENT: '//' ~[\r\n]* -> skip;
ML_COMMENT: '/*' .*? '*/' -> skip;

// Identifiers and literals
IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
STRING: '"' (~["\\] | '\\' .)* '"' | '\'' (~['\\] | '\\' .)* '\'';
NUMBER: [0-9]+ ('.' [0-9]+)?;
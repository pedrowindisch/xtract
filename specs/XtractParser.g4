parser grammar XtractParser;

options {
    tokenVocab = XtractLexer;
}

program: base? scrape+ EOF;

base: BASE STRING;

scrape: SCRAPE IDENTIFIER LBRACE scrapeBody RBRACE;

scrapeBody: 
    method STRING
    parse+;

parse:
    PARSE documentType LBRACE parseBody RBRACE output?;

parseBody:
    SELECT STRING AS IDENTIFIER LBRACE selectFields RBRACE;

selectFields:
    IDENTIFIER COLON OPTIONAL? (textSelector | attrSelector) selectFields?;

output: OUTPUT (APPEND? (JSON | CSV)) STRING;

textSelector: TEXT LPAREN STRING RPAREN;
attrSelector: ATTR LPAREN STRING COMMA STRING RPAREN;

method: GET;
documentType: HTML;

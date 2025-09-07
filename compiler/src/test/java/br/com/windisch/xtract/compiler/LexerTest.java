package br.com.windisch.xtract.compiler;

import br.com.windisch.xtract.compiler.models.LexerException;
import br.com.windisch.xtract.compiler.models.Token;
import br.com.windisch.xtract.compiler.models.TokenType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LexerTest {
    @Test
    void testSingleTokens() throws LexerException {
        Lexer lexer = new Lexer("(){}:,\"\"");
        List<Token> tokens = lexer.run();
        assertEquals(7, tokens.size());
        assertEquals(TokenType.LPAREN, tokens.get(0).getType());
        assertEquals(TokenType.RPAREN, tokens.get(1).getType());
        assertEquals(TokenType.LBRACE, tokens.get(2).getType());
        assertEquals(TokenType.RBRACE, tokens.get(3).getType());
        assertEquals(TokenType.COLON, tokens.get(4).getType());
        assertEquals(TokenType.COMMA, tokens.get(5).getType());
        assertEquals(TokenType.STRING, tokens.get(6).getType());
    }

    @Test
    void testKeywords() throws LexerException {
        String keywords = "base scrape get call for parse html select as optional output append json csv text attr";
        Lexer lexer = new Lexer(keywords);
        List<Token> tokens = lexer.run();
        assertEquals(16, tokens.size());
        assertEquals(TokenType.BASE, tokens.get(0).getType());
        assertEquals(TokenType.SCRAPE, tokens.get(1).getType());
        assertEquals(TokenType.GET, tokens.get(2).getType());
        assertEquals(TokenType.CALL, tokens.get(3).getType());
        assertEquals(TokenType.FOR, tokens.get(4).getType());
        assertEquals(TokenType.PARSE, tokens.get(5).getType());
        assertEquals(TokenType.HTML, tokens.get(6).getType());
        assertEquals(TokenType.SELECT, tokens.get(7).getType());
        assertEquals(TokenType.AS, tokens.get(8).getType());
        assertEquals(TokenType.OPTIONAL, tokens.get(9).getType());
        assertEquals(TokenType.OUTPUT, tokens.get(10).getType());
        assertEquals(TokenType.APPEND, tokens.get(11).getType());
        assertEquals(TokenType.JSON, tokens.get(12).getType());
        assertEquals(TokenType.CSV, tokens.get(13).getType());
        assertEquals(TokenType.TEXT, tokens.get(14).getType());
        assertEquals(TokenType.ATTR, tokens.get(15).getType());
    }

    @Test
    void testIdentifier() throws LexerException {
        Lexer lexer = new Lexer("foobar");
        List<Token> tokens = lexer.run();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.get(0).getType());
        assertEquals("foobar", tokens.get(0).getLexeme());
    }

    @Test
    void testString() throws LexerException {
        Lexer lexer = new Lexer("\"hello\"");
        List<Token> tokens = lexer.run();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.STRING, tokens.get(0).getType());
        assertEquals("hello", tokens.get(0).getLexeme());
    }

    @Test
    void testUnterminatedString() {
        Lexer lexer = new Lexer("\"unterminated");
        assertThrows(LexerException.class, lexer::run);
    }

    @Test
    void testSingleLineComment() throws LexerException {
        Lexer lexer = new Lexer("// comment\nbase");
        List<Token> tokens = lexer.run();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.BASE, tokens.get(0).getType());
    }

    @Test
    void testMultiLineComment() throws LexerException {
        Lexer lexer = new Lexer("/* comment */ base");
        List<Token> tokens = lexer.run();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.BASE, tokens.get(0).getType());
    }

    @Test
    void testUnterminatedMultiLineComment() {
        Lexer lexer = new Lexer("/* unterminated comment");
        assertThrows(LexerException.class, lexer::run);
    }

    @Test
    void testUnexpectedCharacter() {
        Lexer lexer = new Lexer("$");
        assertThrows(LexerException.class, lexer::run);
    }

    @Test
    void testMultilineStringNotSupported() {
        Lexer lexer = new Lexer("\"hello\nworld\"");
        assertThrows(LexerException.class, lexer::run);
    }
}

package org.example.jsonparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

    @Test
    void readsLeftBrace() throws IOException {
        var reader = new StringReader("{");
        Lexer lexer = new Lexer(reader);

        Token token = lexer.nextToken();

        assertEquals(TokenType.LBRACE, token.type());
        assertEquals("{", token.lexeme());
    }

    @Test
    void readsRightBrace() throws IOException {
        var reader = new StringReader("}");
        Lexer lexer = new Lexer(reader);

        Token token = lexer.nextToken();

        assertEquals(TokenType.RBRACE, token.type());
        assertEquals("}", token.lexeme());
    }

    @Test
    void readsEofAfterBrace() throws IOException {
        var reader = new StringReader("{}");
        Lexer lexer = new Lexer(reader);

        lexer.nextToken();
        lexer.nextToken();
        Token token = lexer.nextToken();

        assertEquals(TokenType.EOF, token.type());
    }

    @Test
    void skipsWhiteSpace() throws IOException {
        var reader = new StringReader("  {   }  ");
        Lexer lexer = new Lexer(reader);

        Token lbrace = lexer.nextToken();
        Token rbrace = lexer.nextToken();
        Token eof = lexer.nextToken();

        assertEquals(TokenType.LBRACE, lbrace.type());
        assertEquals(TokenType.RBRACE, rbrace.type());
        assertEquals(TokenType.EOF, eof.type());
    }

    @Test
    void readStringToken() throws IOException {
        StringReader reader = new StringReader("\"hello\"");
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING, token.type());
        assertEquals("\"hello\"", token.lexeme());
    }

    @Test
    void readStringWithEscaptedQuote() throws IOException {
        StringReader reader = new StringReader("\"He said \\\"hi\\\"\"");
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING, token.type());
        assertEquals("\"He said \\\"hi\\\"\"", token.lexeme());
    }
}

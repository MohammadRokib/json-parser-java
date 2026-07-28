package org.example.jsonparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LexerTest {

    @Test
    public void readsLeftBrace() throws IOException {
        assertTokenLexeme("{", TokenType.LBRACE, "LBRACE");
    }

    @Test
    public void readsRightBrace() throws IOException {
        assertTokenLexeme("}", TokenType.RBRACE, "RBRACE");
    }

    @Test
    public void readsEofAfterBrace() throws IOException {
        var reader = new StringReader("{}");
        Lexer lexer = new Lexer(reader);

        lexer.nextToken();
        lexer.nextToken();
        Token token = lexer.nextToken();

        assertEquals(TokenType.EOF, token.type());
    }

    @Test
    public void skipsWhiteSpace() throws IOException {
        var reader = new StringReader("  {   }  ");
        Lexer lexer = new Lexer(reader);

        Token lbrace = lexer.nextToken();
        Token rbrace = lexer.nextToken();
        Token eof = lexer.nextToken();

        assertEquals(TokenType.LBRACE, lbrace.type());
        assertEquals(TokenType.RBRACE, rbrace.type());
        assertEquals(TokenType.EOF, eof.type());
    }

    protected void assertTokenLexeme(String input, TokenType expectedType, String typString) throws IOException {
        StringReader reader = new StringReader(input);
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        Token token = lexer.nextToken();
        assertEquals(expectedType, token.type(), "Token type should be" + typString + " for input: " + input);
        assertEquals(input, token.lexeme(), "Lexeme should match input: " + input);
    }

    protected <T extends Throwable> T assertThrowsOnError(Class<T> expectedType, String input) throws IOException {
        StringReader reader = new StringReader(input);
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        return assertThrows(expectedType, lexer::nextToken);
    }
}

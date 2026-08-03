package org.example.jsonparser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {
    @ParameterizedTest(name = "\"{0}\" is tokenized as {1}")
    @CsvSource({
            "{, LBRACE",
            "}, RBRACE",
            "[, LBRACKET",
            "], RBRACKET"
    })
    public void readsSingleCharacterToken(String input, TokenType expectedType) throws IOException {
        LexerAssertions.assertTokenLexeme(input, expectedType);
    }

    @Test
    public void readsEofAfterBrace() throws IOException {
        StringReader reader = new StringReader("{}");
        Lexer lexer = new Lexer(reader);

        lexer.nextToken();
        lexer.nextToken();
        Token token = lexer.nextToken();

        assertEquals(TokenType.EOF, token.type());
    }

    @Test
    public void skipWhiteSpace() throws IOException {
        StringReader reader = new StringReader("  {  }  ");
        Lexer lexer = new Lexer(reader);

        Token lbrace = lexer.nextToken();
        Token rbrace = lexer.nextToken();
        Token eof    = lexer.nextToken();

        assertEquals(TokenType.LBRACE, lbrace.type());
        assertEquals(TokenType.RBRACE, rbrace.type());
        assertEquals(TokenType.EOF, eof.type());
    }
}

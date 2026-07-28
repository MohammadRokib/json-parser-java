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

    @Test
    public void readStringToken() throws IOException {
        assertTokenLexeme("\"hello\"", TokenType.STRING, "STRING");
    }

    @Test
    public void readEmptyStringToken() throws IOException {
        assertTokenLexeme("\"\"", TokenType.STRING, "STRING");
    }


    @Test
    public void readStringWithEscaptedQuote() throws IOException {
        assertTokenLexeme("\"He said \\\"hi\\\"\"", TokenType.STRING, "STRING");
    }

    @Test
    public void throwsOnUnterminatedString() throws IOException {
        assertThrowsOnError(JsonParseException.class, "\"hello");
    }

    @Test
    public void readZero() throws IOException {
        assertTokenLexeme("0", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readNegativeZero() throws IOException {
        assertTokenLexeme("-0", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readPositiveInteger() throws IOException {
        assertTokenLexeme("42", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readNegativeInteger() throws IOException {
        assertTokenLexeme("-42", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readPositiveDouble() throws IOException {
        assertTokenLexeme("3.14159", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readNegativeDouble() throws IOException {
        assertTokenLexeme("-3.14159", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readZeroWithDecimal() throws IOException {
        assertTokenLexeme("0.0001", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readNegativeZeroWithDecimal() throws IOException {
        assertTokenLexeme("-0.0", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readPositiveScientific() throws IOException {
        assertTokenLexeme("2e10", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readNegativeScientific() throws IOException {
        assertTokenLexeme("-2e10", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readUppercaseScientific() throws IOException {
        assertTokenLexeme("2E10", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readPositiveScientificWithDecimal() throws IOException {
        assertTokenLexeme("1.23e10", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readNegativeScientificWithDecimal() throws IOException {
        assertTokenLexeme("-1.23e10", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readPositiveScientificWithPlusSign() throws IOException {
        assertTokenLexeme("1.23e+5", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readNegativeScientificWithPlusSign() throws IOException {
        assertTokenLexeme("-1.23e+5", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readUppercaseScientificWithPlusSign() throws IOException {
        assertTokenLexeme("1.23E+10", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readPositiveScientificWithMinusSign() throws IOException {
        assertTokenLexeme("1e-5", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readNegativeScientificWithMinusSign() throws IOException {
        assertTokenLexeme("-1.23e-5", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readUppercaseScientificWithMinusSign() throws IOException {
        assertTokenLexeme("-1.23E-5", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readZeroWithExponent() throws IOException {
        assertTokenLexeme("0e5", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readZeroWithNegativeExponent() throws IOException {
        assertTokenLexeme("0e-5", TokenType.NUMBER, "NUMBER");
    }

    @Test
    public void readsTrue() throws IOException {
        assertTokenLexeme("true", TokenType.TRUE, "TRUE");
    }

    @Test
    public void readsFalse() throws IOException {
        assertTokenLexeme("false", TokenType.FALSE, "FALSE");
    }

    @Test
    public void readsNull() throws IOException {
        assertTokenLexeme("null", TokenType.NULL, "NULL");
    }

    @Test
    public void throwsOnInvalidKeyword() throws IOException {
        assertThrowsOnError(JsonParseException.class, "truee");
    }

    private void assertTokenLexeme(String input, TokenType expectedType, String typString) throws IOException {
        StringReader reader = new StringReader(input);
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        Token token = lexer.nextToken();
        assertEquals(expectedType, token.type(), "Token type should be" + typString + " for input: " + input);
        assertEquals(input, token.lexeme(), "Lexeme should match input: " + input);
    }

    private <T extends Throwable> T assertThrowsOnError(Class<T> expectedType, String input) throws IOException {
        StringReader reader = new StringReader(input);
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        return assertThrows(expectedType, lexer::nextToken);
    }
}

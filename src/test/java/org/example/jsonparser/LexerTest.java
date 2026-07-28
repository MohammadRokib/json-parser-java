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
        var reader = new StringReader("{");
        Lexer lexer = new Lexer(reader);

        Token token = lexer.nextToken();

        assertEquals(TokenType.LBRACE, token.type());
        assertEquals("{", token.lexeme());
    }

    @Test
    public void readsRightBrace() throws IOException {
        var reader = new StringReader("}");
        Lexer lexer = new Lexer(reader);

        Token token = lexer.nextToken();

        assertEquals(TokenType.RBRACE, token.type());
        assertEquals("}", token.lexeme());
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
        StringReader reader = new StringReader("\"hello\"");
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING, token.type());
        assertEquals("\"hello\"", token.lexeme());
    }

    @Test
    public void readEmptyStringToken() throws IOException {
        StringReader reader = new StringReader("\"\"");
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING, token.type());
        assertEquals("\"\"", token.lexeme());
    }


    @Test
    public void readStringWithEscaptedQuote() throws IOException {
        StringReader reader = new StringReader("\"He said \\\"hi\\\"\"");
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING, token.type());
        assertEquals("\"He said \\\"hi\\\"\"", token.lexeme());
    }

    @Test
    public void throwsOnUnterminatedString() throws IOException {
        StringReader reader = new StringReader("\"hello");
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        assertThrows(JsonParseException.class, lexer::nextToken);
    }@Test
    public void readZero() throws IOException {
        assertNumberLexeme("0");
    }

    @Test
    public void readNegativeZero() throws IOException {
        assertNumberLexeme("-0");
    }

    @Test
    public void readPositiveInteger() throws IOException {
        assertNumberLexeme("42");
    }

    @Test
    public void readNegativeInteger() throws IOException {
        assertNumberLexeme("-42");
    }

    @Test
    public void readPositiveDouble() throws IOException {
        assertNumberLexeme("3.14159");
    }

    @Test
    public void readNegativeDouble() throws IOException {
        assertNumberLexeme("-3.14159");
    }

    @Test
    public void readZeroWithDecimal() throws IOException {
        assertNumberLexeme("0.0001");
    }

    @Test
    public void readNegativeZeroWithDecimal() throws IOException {
        assertNumberLexeme("-0.0");
    }

    @Test
    public void readPositiveScientific() throws IOException {
        assertNumberLexeme("2e10");
    }

    @Test
    public void readNegativeScientific() throws IOException {
        assertNumberLexeme("-2e10");
    }

    @Test
    public void readUppercaseScientific() throws IOException {
        assertNumberLexeme("2E10");
    }

    @Test
    public void readPositiveScientificWithDecimal() throws IOException {
        assertNumberLexeme("1.23e10");
    }

    @Test
    public void readNegativeScientificWithDecimal() throws IOException {
        assertNumberLexeme("-1.23e10");
    }

    @Test
    public void readPositiveScientificWithPlusSign() throws IOException {
        assertNumberLexeme("1.23e+5");
    }

    @Test
    public void readNegativeScientificWithPlusSign() throws IOException {
        assertNumberLexeme("-1.23e+5");
    }

    @Test
    public void readUppercaseScientificWithPlusSign() throws IOException {
        assertNumberLexeme("1.23E+10");
    }

    @Test
    public void readPositiveScientificWithMinusSign() throws IOException {
        assertNumberLexeme("1e-5");
    }

    @Test
    public void readNegativeScientificWithMinusSign() throws IOException {
        assertNumberLexeme("-1.23e-5");
    }

    @Test
    public void readUppercaseScientificWithMinusSign() throws IOException {
        assertNumberLexeme("-1.23E-5");
    }

    @Test
    public void readZeroWithExponent() throws IOException {
        assertNumberLexeme("0e5");
    }

    @Test
    public void readZeroWithNegativeExponent() throws IOException {
        assertNumberLexeme("0e-5");
    }

    private void assertNumberLexeme(String input) throws IOException {
        StringReader reader = new StringReader(input);
        PushbackReader pbReader = new PushbackReader(reader);
        Lexer lexer = new Lexer(pbReader);

        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.type(), "Token type should be NUMBER for input: " + input);
        assertEquals(input, token.lexeme(), "Lexeme should match input: " + input);
    }
}

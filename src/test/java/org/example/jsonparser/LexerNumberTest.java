package org.example.jsonparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LexerNumberTest extends LexerTest {
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

    /*@Test
    public void throwsOnInvalidNumber() throws IOException {
        assertThrowsOnError(JsonParseException.class, "-.e-");
    }*/
}

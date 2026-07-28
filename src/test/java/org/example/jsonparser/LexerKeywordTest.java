package org.example.jsonparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LexerKeywordTest extends LexerTest {
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
}

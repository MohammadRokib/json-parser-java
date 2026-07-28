package org.example.jsonparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LexerStringTest extends LexerTest {
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
}

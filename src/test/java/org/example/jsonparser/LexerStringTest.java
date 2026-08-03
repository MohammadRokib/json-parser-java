package org.example.jsonparser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

public class LexerStringTest {
    @ParameterizedTest(name = "{0} is a valid string token")
    @ValueSource(strings = {
            "\"hello\"",
            "\"\"",
            "\"He said \\\"hi\\\"\""
    })
    public void readsValidStringToken(String input) throws IOException {
        LexerAssertions.assertTokenLexeme(input, TokenType.STRING);
    }

    @Test
    public void throwsOnUnterminatedString() throws IOException {
        LexerAssertions.assertThrowsOnError(JsonParseException.class, "\"hello");
    }
}

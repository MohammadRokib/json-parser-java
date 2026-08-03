package org.example.jsonparser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

public class LexerKeywordTest {
    @ParameterizedTest(name = "\"{0}\" is tokenized as {1}")
    @CsvSource({
            "true,  TRUE",
            "false, FALSE",
            "null,  NULL"
    })
    public void readsKeyword(String input, TokenType expectedType) throws IOException {
        LexerAssertions.assertTokenLexeme(input, expectedType);
    }

    @Test
    public void throwsOnInvalidKeyword() throws IOException {
        LexerAssertions.assertThrowsOnError(JsonParseException.class, "truee");
    }
}

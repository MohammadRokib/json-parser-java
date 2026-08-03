package org.example.jsonparser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

public class LexerNumberTest {
    @ParameterizedTest(name = "\"{0}\" is a valid number")
    @ValueSource(strings = {
            "0", "-0",
            "42", "-42",
            "3.14159", "-3.14159",
            "0.0001", "-0.0",
            "2e10", "-2e10", "2E10",
            "1.23e10", "-1.23e10",
            "1.23e+5", "-1.23e+5", "1.23E+10",
            "1e-5", "-1.23e-5", "-1.23E-5",
            "0e5", "0e-5"
    })
    public void readsValidNumber(String input) throws IOException {
        LexerAssertions.assertTokenLexeme(input, TokenType.NUMBER);
    }

    @Test
    public void throwsOnInvalidNumber() throws IOException {
        LexerAssertions.assertThrowsOnError(JsonParseException.class, "-.e-");
    }
}

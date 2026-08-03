package org.example.jsonparser;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LexerAssertions {
    private LexerAssertions()  {}

    public static void assertTokenLexeme(String input, TokenType expectedType) throws IOException {
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);

        Token token = lexer.nextToken();
        assertEquals(expectedType, token.type(), "Token type should be " + expectedType + " for input " + input);
        assertEquals(input, token.lexeme(), "Lexeme should match input " + input);
    }

    public static<T extends Throwable> T assertThrowsOnError(Class<T> expectedType, String input) throws IOException {
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);

        return assertThrows(expectedType, lexer::nextToken);
    }
}

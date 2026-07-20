package org.example.jsonparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    @Test
    public void parseEmptyObject() throws IOException {
        StringReader reader = new StringReader("{}");
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(parser::parse);
    }

    @Test
    public void parseEmptyObjectWithWhiteSpace() throws IOException {
        StringReader reader = new StringReader("  {   }  ");
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(parser::parse);
    }

    @Test
    public void throwsOnMissingRightBrace() throws IOException {
        StringReader reader = new StringReader("{");
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);

        assertThrows(JsonParseException.class, parser::parse);
    }

    @Test
    public void throwsOnTrailingGarbage() throws IOException {
        StringReader reader = new StringReader("{ } }");
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);

        assertThrows(JsonParseException.class, parser::parse);
    }
}

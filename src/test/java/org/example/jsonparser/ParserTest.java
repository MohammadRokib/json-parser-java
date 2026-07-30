package org.example.jsonparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    @Test
    public void parseEmptyObject() throws IOException {
        assertThrowNotThrow("{}", false);
    }

    @Test
    public void parseEmptyObjectWithWhiteSpace() throws IOException {
        assertThrowNotThrow("  {   }  ", false);
    }

    @Test
    public void throwsOnMissingRightBrace() throws IOException {
        assertThrowNotThrow("{", true);
    }

    @Test
    public void throwsOnTrailingGarbage() throws IOException {
        assertThrowNotThrow("{ } }", true);
    }

    @Test
    public void parseSingleKeyValue() throws IOException {
        assertThrowNotThrow("{\"key\": \"value\"}", false);
    }

    @Test
    public void parseMultipleKeyValue() throws IOException {
        assertThrowNotThrow("{\"key1\": \"value1\", \"key2\": \"value2\"}", false);
    }

    @Test
    public void parseThrowOnMissingColon() throws IOException {
        assertThrowNotThrow("{\"key\" \"value\"}", true);
    }

    @Test
    public void parseThrowOnMissingComma() throws IOException {
        assertThrowNotThrow("{\"key1\": \"value1\" \"key2\": \"value2\"}", true);
    }

    @Test
    public void parseThrowOnMissingLBrace() throws IOException {
        assertThrowNotThrow("\"key1\": \"value1\", \"key2\": \"value2\"}", true);
    }

    @Test
    public void parseThrowOnMissingRBrace() throws IOException {
        assertThrowNotThrow("{\"key1\": \"value1\", \"key2\": \"value2\"", true);
    }

    @Test
    public void parseMixedKeyValue() throws IOException {
        assertThrowNotThrow("{\"string\": \"String\"," +
                            "\"number\": 123.5436," +
                            "\"right\": true," +
                            "\"wrong\": false," +
                            "\"none\": null}", false);
    }

    @Test
    public void throwsOnWrongMixedKeyValue() throws IOException {
        assertThrowNotThrow("{\"string\": \"String\"," +
                "\"number\": 123.5436," +
                "\"right\": trues," +
                "\"wrong\": false," +
                "\"none\": null}", true);
    }

    private void assertThrowNotThrow(String input, boolean shouldThrow) throws IOException {
        StringReader reader = new StringReader(input);
        PushbackReader pbReader = new PushbackReader(reader);

        Lexer lexer = new Lexer(pbReader);
        Parser parser = new Parser(lexer);

        if (shouldThrow) {
            assertThrows(JsonParseException.class, parser::parse);
        } else {
            assertDoesNotThrow(parser::parse);
        }
    }
}

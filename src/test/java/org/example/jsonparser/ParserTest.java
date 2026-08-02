package org.example.jsonparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
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
    public void parseObjectWithWhiteSpaceValue() throws IOException {
        assertThrowNotThrow("{\"value\": \" \"}", false);
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
    public void parseArray() throws IOException {
        assertThrowNotThrow("{\"array\": [1, true, false, null, 0.45, \"name\"]}", false);
    }

    @Test
    public void parseThrowsOnInvalidArray() throws IOException {
        assertThrowNotThrow("{\"array\": [1 true false null 0.45 \"name\"]}", true);
    }

    @Test
    public void parseNestedObject() throws IOException {
        String input = """
                {
                    "parentKey1": "parentValue1",
                	"nestedValue1": {
                	    "key1": "value1",
                		"key2": "value2"
                	},
                
                    "array1": [1, 2, 3, 4, 5, "name"],
                    "number1": -14.6e-45,
                    "parentKey2": "parentValue2"
                }
                """;

        assertThrowNotThrow(input, false);
    }

    @Test
    public void throwsOnInvalidNestedObject() throws IOException {
        String input = """
                {
                    "parentKey1": "parentValue1",
                	"nestedValue1": {
                	    "key1": "value1",
                		"key2": "value2"
                	},
                
                    "array1": [1, 2, 3, 4, 5, "name],
                	"number1": -14.e-45,
                	"parentKey2": "parentValue2
                }
                """;

        assertThrowNotThrow(input, true);
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

        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);

        if (shouldThrow) {
            assertThrows(JsonParseException.class, parser::parse);
        } else {
            assertDoesNotThrow(parser::parse);
        }
    }
}

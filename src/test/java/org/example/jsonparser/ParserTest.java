package org.example.jsonparser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    private static Stream<Arguments> validJson() {
        return Stream.of(
                Arguments.of("empty object", "{}"),
                Arguments.of("empty object with whitespace", "  {   }  "),
                Arguments.of("object with whitespace-only value", "{\"value\": \" \"}"),
                Arguments.of("single key/value pair", "{\"key\": \"value\"}"),
                Arguments.of("multiple key/value pairs", "{\"key1\": \"value1\", \"key2\": \"value2\"}"),
                Arguments.of("array value", "{\"array\": [1, true, false, null, 0.45, \"name\"]}"),
                Arguments.of("mixed value types", "{\"string\": \"String\"," +
                        "\"number\": 123.5436," +
                        "\"right\": true," +
                        "\"wrong\": false," +
                        "\"none\": null}")
        );
    }

    private static Stream<Arguments> invalidJson() {
        return Stream.of(
                Arguments.of("missing right brace", "{"),
                Arguments.of("trailing garbage after object", "{ } }"),
                Arguments.of("missing colon between key and value", "{\"key\" \"value\"}"),
                Arguments.of("missing comma between pairs", "{\"key1\": \"value1\" \"key2\": \"value2\"}"),
                Arguments.of("missing left brace", "\"key1\": \"value1\", \"key2\": \"value2\"}"),
                Arguments.of("missing right brace with content", "{\"key1\": \"value1\", \"key2\": \"value2\""),
                Arguments.of("array missing commas between values", "{\"array\": [1 true false null 0.45 \"name\"]}"),
                Arguments.of("invalid keyword among mixed values", "{\"string\": \"String\"," +
                        "\"number\": 123.5436," +
                        "\"right\": trues," +
                        "\"wrong\": false," +
                        "\"none\": null}")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("validJson")
    public void parsesValidJson(String description, String input) throws IOException {
        assertThrowNotThrow(input, false);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidJson")
    public void throwsOnInvalidJson(String description, String input) throws IOException {
        assertThrowNotThrow(input, true);
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

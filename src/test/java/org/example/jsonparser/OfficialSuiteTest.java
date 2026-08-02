package org.example.jsonparser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OfficialSuiteTest {
    public static Stream<Arguments> jsonTestFileProvider() throws IOException {
        Path testDir = Paths.get("src/test/resources/json_checker");
        return Files.list(testDir)
                .map(path -> Arguments.of(path.getFileName().toString(), path));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("jsonTestFileProvider")
    public void runOfficialTestSuite(String fileName, Path filePath) throws IOException {
        String content = Files.readString(filePath);

        StringReader reader = new StringReader(content);

        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);

        if (fileName.startsWith("pass")) {
            assertDoesNotThrow(parser::parse, "Failed on valid file: " + fileName);
        } else if (fileName.startsWith("fail")) {
            assertThrows(JsonParseException.class, parser::parse, "Should have failed on invalid file: " + fileName);
        }
    }
}

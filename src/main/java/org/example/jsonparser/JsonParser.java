package org.example.jsonparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonParser {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java JsonParser <file.json>");
            System.exit(1);
        }

        Path path = Path.of(args[0]);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            PushbackReader reader = new PushbackReader(bufferedReader);

            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser(lexer);

            parser.parse();
            System.out.println("Valid JSON");
            System.exit(0);
        } catch (JsonParseException e) {
            System.err.println("Invalid JSON: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }
}
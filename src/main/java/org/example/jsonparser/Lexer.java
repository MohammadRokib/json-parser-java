package org.example.jsonparser;

import java.io.IOException;
import java.io.Reader;

public class Lexer {
    private final Reader reader;
    private int currentChar;
    private int line = 1;
    private int column = 0;

    public  Lexer(Reader reader) throws IOException {
        this.reader = reader;
        advance();
    }

    public Token nextToken() throws IOException {
        skipWhiteSpace();

        int startLine = line;
        int startColumn = column;

        if (currentChar == -1) {
            return new Token(TokenType.EOF, "", startLine, startColumn);
        }

        char c = (char) currentChar;
        return switch (c) {
            case '{' -> {
                advance();
                yield new Token(TokenType.LBRACE, "{", startLine, startColumn);
            }
            case '}' -> {
                advance();
                yield new Token(TokenType.RBRACE, "}", startLine, startColumn);
            }
            default -> throw new RuntimeException(
                    "Unexpected character: '" + c + "' at line " + startLine + ", column " + startColumn
            );
        };
    }

    public void advance() throws IOException {
        currentChar = reader.read();
        if (currentChar == '\n') {
            line++;
            column = 0;
        } else if (currentChar != -1) {
            column++;
        }
    }

    private void skipWhiteSpace() throws IOException {
        while (currentChar != -1 && Character.isWhitespace(currentChar)) {
            advance();
        }
    }
}

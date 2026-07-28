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

        if (Character.isDigit(c) || c == '-') {
            return readNumber(startLine, startColumn);
        }

        return switch (c) {
            case '{' -> {
                advance();
                yield new Token(TokenType.LBRACE, "{", startLine, startColumn);
            }
            case '}' -> {
                advance();
                yield new Token(TokenType.RBRACE, "}", startLine, startColumn);
            }
            case ':' -> {
                advance();
                yield new Token(TokenType.COLON, ":", startLine, startColumn);
            }
            case ',' -> {
                advance();
                yield new Token(TokenType.COMMA, ",", startLine, startColumn);
            }
            case '"' -> readString(startLine, startColumn);
            default -> throw new JsonParseException(
                    "Unexpected character: '" + c,
                    startLine,
                    startColumn
            );
        };
    }

    private Token readString(int startLine, int startColumn) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        advance();

        while (currentChar != '"' && currentChar != -1) {
            if (currentChar == '\\') {
                sb.append('\\');
                advance();

                if (currentChar == -1) {
                    throw new JsonParseException("Unterminated string", startLine, startColumn);
                }
            }
            sb.append((char) currentChar);
            advance();
        }

        if (currentChar == -1) {
            throw new JsonParseException("Unterminated string", startLine, startColumn);
        }
        sb.append('"');
        advance();

        return new Token(TokenType.STRING, sb.toString(), startLine, startColumn);
    }

    private Token readNumber(int startLine, int startColumn) throws IOException {
        StringBuilder sb = new StringBuilder();
        if ((char) currentChar == '-') {
            sb.append((char) currentChar);
            advance();
        }

        while (currentChar != -1 && Character.isDigit((char) currentChar)) {
            sb.append((char) currentChar);
            advance();
        }

        if ((char) currentChar == '.') {
            do {
                sb.append((char) currentChar);
                advance();
            } while (currentChar != -1 && Character.isDigit((char) currentChar));
        }

        if ((char) currentChar == 'e' || (char) currentChar == 'E') {
            sb.append((char) currentChar);
            advance();

            if ((char) currentChar == '+' || (char) currentChar == '-') {
                sb.append((char) currentChar);
                advance();
            }

            while (currentChar != -1 && Character.isDigit((char) currentChar)) {
                sb.append((char) currentChar);
                advance();
            }
        }

        return new Token(TokenType.NUMBER, sb.toString(), startLine, startColumn);
    }

    private void advance() throws IOException {
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

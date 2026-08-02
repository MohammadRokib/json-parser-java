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

        if (Character.isLetter(c)) {
            return readKeyword(startLine, startColumn);
        }

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
            case '[' -> {
                advance();
                yield new Token(TokenType.LBRACKET, "[", startLine, startColumn);
            }
            case ']' -> {
                advance();
                yield new Token(TokenType.RBRACKET, "]", startLine, startColumn);
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

        String escapeCharacters = "\"\\/bfnrtu";

        while (currentChar != -1 && currentChar != '"') {
            if (currentChar == '\n') {
                throw new JsonParseException(
                        "Invalid new line character inside string",
                        startLine, startColumn
                );
            }
            if (currentChar == '\\') {
                sb.append('\\');
                advance();

                if (currentChar == -1) {
                    throw new JsonParseException("Unterminated string", startLine, startColumn);
                }

                char escapedChar = (char) currentChar;
                if (escapeCharacters.indexOf(escapedChar) < 0) {
                    throw new JsonParseException(
                            "Invalid escape sequence: \\" + escapedChar,
                            startLine, startColumn
                    );
                }

                if (escapedChar == 'u') {
                    sb.append('u');
                    advance();

                    for (int i = 0; i < 4; i++) {
                        if (currentChar == -1 || !isHexDigit(currentChar)) {
                            throw new JsonParseException(
                                    "Invalid unicode escape sequence",
                                    startLine, startColumn
                            );
                        }
                        sb.append((char) currentChar);
                        advance();
                    }
                    continue;
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

        if (currentChar != -1 && !Character.isDigit((char) currentChar)) {
            throw new JsonParseException("Invalid number: missing integer part", startLine, startColumn);
        }

        if ((char) currentChar == '0') {
            sb.append((char) currentChar);
            advance();

            if (currentChar != -1 && Character.isDigit((char) currentChar)) {
                throw new JsonParseException("Invalid number: leading zeros are not allowed", startLine, startColumn);
            }
        } else {
            while (currentChar != -1 && Character.isDigit((char) currentChar)) {
                sb.append((char) currentChar);
                advance();
            }
        }

        if ((char) currentChar == '.') {
            sb.append((char) currentChar);
            advance();

            if (currentChar != -1 && !Character.isDigit((char) currentChar)) {
                throw new JsonParseException("Invalid number: missing digits after decimal", startLine, startColumn);
            }

            while (currentChar != -1 && Character.isDigit((char) currentChar)) {
                sb.append((char) currentChar);
                advance();
            }
        }

        if ((char) currentChar == 'e' || (char) currentChar == 'E') {
            sb.append((char) currentChar);
            advance();

            if ((char) currentChar == '+' || (char) currentChar == '-') {
                sb.append((char) currentChar);
                advance();
            }

            if (currentChar != -1 && !Character.isDigit((char) currentChar)) {
                throw new JsonParseException("Invalid number: missing digits in exponent", startLine, startColumn);
            }

            while (currentChar != -1 && Character.isDigit((char) currentChar)) {
                sb.append((char) currentChar);
                advance();
            }
        }

        return new Token(TokenType.NUMBER, sb.toString(), startLine, startColumn);
    }

    private Token readKeyword(int startLine, int startColumn) throws IOException {
        StringBuilder sb = new StringBuilder();

        char c = (char) currentChar;
        while (currentChar != -1 && Character.isLetter(c)) {
            sb.append(c);
            advance();
            c = (char) currentChar;
        }

        String lexeme = sb.toString();
        return switch (lexeme) {
            case "true" -> new Token(TokenType.TRUE, lexeme, startLine, startColumn);
            case "false" -> new Token(TokenType.FALSE, lexeme, startLine, startColumn);
            case "null" -> new Token(TokenType.NULL, lexeme, startLine, startColumn);
            default -> throw new JsonParseException(
                    "Unexpected keyword: '" + lexeme, startLine, startColumn
            );
        };
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

    private boolean isHexDigit(int c) {
        return ((c >= '0' && c <= '9') ||
                (c >= 'a' && c <= 'f') ||
                (c >= 'A' && c <= 'F'));
    }
}

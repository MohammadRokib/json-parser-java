package org.example.jsonparser;

import java.io.IOException;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;

    private int currentDepth = 0;
    private static final int MAX_DEPTH = 20;

    public Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        currentToken = lexer.nextToken();
    }

    public void parse() throws IOException {
        if (currentToken.type() == TokenType.LBRACE) {
            parseObject();
        } else if (currentToken.type() == TokenType.LBRACKET) {
            currentDepth++;
            if (currentDepth > MAX_DEPTH) {
                throw new JsonParseException(
                        "Maximum nesting depth exceeded",
                        currentToken.line(), currentToken.column()
                );
            }
            parseArray();
        }
        eat(TokenType.EOF);
    }

    private void parseObject() throws IOException {
        eat(TokenType.LBRACE);
        if (currentToken.type() != TokenType.RBRACE) {
            parsePairs();
        }
        eat(TokenType.RBRACE);
    }

    private void parsePairs() throws IOException {
        while(true) {
            eat(TokenType.STRING);
            eat(TokenType.COLON);
            parseValue();

            if (currentToken.type() == TokenType.COMMA) {
                eat(TokenType.COMMA);
            } else {
                break;
            }
        }
    }

    private void parseValue() throws IOException {
        switch (currentToken.type()) {
            case STRING:
            case NUMBER:
            case TRUE:
            case FALSE:
            case NULL:
                eat(currentToken.type());
                break;
            case LBRACE:
                parseObject();
                break;
            case LBRACKET:
                currentDepth++;
                if (currentDepth >= MAX_DEPTH) {
                    throw new JsonParseException(
                            "Maximum nesting depth exceeded",
                            currentToken.line(), currentToken.column()
                    );
                }
                parseArray();
                currentDepth--;
                break;
            default:
                throw new JsonParseException(
                        "Expected a value but found " + currentToken.type(),
                        currentToken.line(), currentToken.column()
                );
        }
    }

    private void parseArray() throws IOException {
        eat(TokenType.LBRACKET);
        while(currentToken.type() != TokenType.RBRACKET) {
            parseValue();
            if (currentToken.type() == TokenType.COMMA) {
                eat(TokenType.COMMA);
                if (currentToken.type() == TokenType.RBRACKET) {
                    throw new JsonParseException(
                            "Expected a value but found" + currentToken.type(),
                            currentToken.line(), currentToken.column()
                    );
                }
            } else {
                break;
            }
        }

        eat(TokenType.RBRACKET);
    }

    private void eat(TokenType type) throws IOException {
        if (currentToken.type() == type) {
            currentToken = lexer.nextToken();
        } else {
            throw new JsonParseException(
                    "Expected " + type + " but found " + currentToken.type(),
                    currentToken.line(),
                    currentToken.column()
            );
        }
    }
}

package org.example.jsonparser;

import java.io.IOException;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        currentToken = lexer.nextToken();
    }

    public void parse() throws IOException {
        parseObject();
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
            eat(TokenType.STRING);

            if (currentToken.type() == TokenType.COMMA) {
                eat(TokenType.COMMA);
            } else {
                break;
            }
        }
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

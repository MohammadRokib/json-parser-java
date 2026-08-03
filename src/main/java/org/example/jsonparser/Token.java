package org.example.jsonparser;

public record Token(TokenType type, String lexeme, int line, int column) {}

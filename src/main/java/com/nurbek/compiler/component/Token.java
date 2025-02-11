package com.nurbek.compiler.component;

public interface Token {

    TokenType type();

    String text();

    enum TokenType {
        NUMBER, STRING,

        // keywords
        PRINT, INPUT, LET,
        IF, THEN, ENDIF,
        WHILE, REPEAT, ENDWHILE,

        // operators
        EQUAL, PLUS, MINUS, ASTERISK, SLASH, MODULO,

        // comparison
        EQUALS, GREATER, LESS, GREATER_EQUAL, LESS_EQUAL, AND, OR, NOTEQUAL,

        NEWLINE, EOF, IDENTIFIER,

    }

}
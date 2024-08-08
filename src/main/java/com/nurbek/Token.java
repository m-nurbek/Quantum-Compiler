package com.nurbek;

public interface Token {

    TokenType getType();

    String getText();

    enum TokenType {
        NUMBER, STRING,

        // keywords
        LABEL, GOTO, PRINT, INPUT, LET,
        IF, THEN, ENDIF,
        WHILE, REPEAT, ENDWHILE,

        // operators
        EQUAL, PLUS, MINUS, ASTERISK, SLASH, MODULO,

        // comparison
        EQUALS, GREATER, LESS, GREATER_EQUAL, LESS_EQUAL, AND, OR, NOTEQUAL,

        NEWLINE, EOF, IDENTIFIER,

    }

}
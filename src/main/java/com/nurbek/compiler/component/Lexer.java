package com.nurbek.compiler.component;

public interface Lexer {

    void setSource(String string);

    // get next token
    Token nextToken();

    // proceed to next character
    void nextChar();

    // look ahead to the next character without updating the current position
    char peekChar();

    // log the error message
    void abort(String message);

    // skip the whitespace characters
    void skipWhitespace();

    // skip comments
    void skipComment();

    char getCurrChar();

    int getCurrPos();
}
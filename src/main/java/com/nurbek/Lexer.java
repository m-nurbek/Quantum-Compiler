package com.nurbek;

public interface Lexer {

    // get next token
    Token nextToken() throws LexerException;

    // proceed to next character
    void nextChar();

    // look ahead to the next character without updating the current position
    char peekChar();

    // log the error message
    void abort(String message) throws LexerException;

    // skip the whitespace characters
    void skipWhitespace();

    // skip comments
    void skipComment();

    char getCurrChar();

    int getCurrPos();

}
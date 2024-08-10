package com.nurbek;

public interface Parser {

    boolean checkToken(Token.TokenType type);

    boolean checkPeek(Token.TokenType type);

    void match(Token.TokenType type) throws ParserException, LexerException;

    void nextToken() throws LexerException;

    void abort(String message) throws ParserException;

    void program() throws LexerException, ParserException;

}
package com.nurbek.compiler.component;

public interface Parser {

    boolean checkToken(Token.TokenType type);

    boolean checkPeek(Token.TokenType type);

    void match(Token.TokenType type);

    void nextToken();

    void abort(String message);

    void program();

    void setLexer(Lexer lexer);

    void setEmitter(Emitter emitter);
}
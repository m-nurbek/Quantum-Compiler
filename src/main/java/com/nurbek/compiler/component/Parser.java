package com.nurbek.compiler.component;

import com.nurbek.compiler.exception.LexerException;
import com.nurbek.compiler.exception.ParserException;

public interface Parser {

    boolean checkToken(Token.TokenType type);

    boolean checkPeek(Token.TokenType type);

    void match(Token.TokenType type) throws ParserException, LexerException;

    void nextToken() throws LexerException;

    void abort(String message) throws ParserException;

    void program() throws LexerException, ParserException;

    void setLexer(Lexer lexer);

    void setEmitter(Emitter emitter);
}
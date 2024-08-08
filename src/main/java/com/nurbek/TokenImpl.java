package com.nurbek;

public class TokenImpl implements Token {
    private final TokenType type;
    private final String text;

    public TokenImpl(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }

    @Override
    public TokenType getType() {
        return type;
    }

    @Override
    public String getText() {
        return text;
    }
}
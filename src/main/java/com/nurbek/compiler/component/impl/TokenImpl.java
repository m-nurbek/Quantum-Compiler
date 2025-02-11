package com.nurbek.compiler;

public record TokenImpl(String text, TokenType type) implements Token {
}
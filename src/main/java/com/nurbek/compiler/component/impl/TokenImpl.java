package com.nurbek.compiler.component.impl;

import com.nurbek.compiler.component.Token;

public record TokenImpl(String text, TokenType type) implements Token {
}
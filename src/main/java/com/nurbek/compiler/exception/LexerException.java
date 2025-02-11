package com.nurbek.compiler.exception;

import java.io.Serial;

public class LexerException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public LexerException(String message) {
        super(message);
    }
}
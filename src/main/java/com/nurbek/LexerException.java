package com.nurbek;

import java.io.Serial;

public class LexerException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public LexerException(String message) {
        super(message);
    }
}
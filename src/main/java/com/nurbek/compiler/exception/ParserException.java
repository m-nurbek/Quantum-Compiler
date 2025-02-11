package com.nurbek.compiler.exception;

import java.io.Serial;

public class ParserException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2L;

    public ParserException(String message) {
        super(message);
    }
}
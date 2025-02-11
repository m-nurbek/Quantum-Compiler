package com.nurbek.compiler.exceptions;

import java.io.Serial;

public class ParserException extends Exception {
    @Serial
    private static final long serialVersionUID = 2L;

    public ParserException(String message) {
        super(message);
    }
}
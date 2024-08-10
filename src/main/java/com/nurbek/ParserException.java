package com.nurbek;

import java.io.Serial;

public class ParserException extends Exception {
    @Serial
    private static final long serialVersionUID = 2L;

    public ParserException(String message) {
        super(message);
    }
}
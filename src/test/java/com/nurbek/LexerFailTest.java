package com.nurbek;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class LexerFailTest {
    private String source;

    public LexerFailTest(String source) {
        this.source = source;
    }

    @Parameterized.Parameters(name = "{index} | source=\" {0} \"")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"<<"},
                {">>="},
                {"=!"},
                {"1ewq"},
                {"+-"},
                {"-+"},
                {"==="},
                {"!!="},
                {"!="}
        });
    }

    @Test(expected = LexerException.class)
    public void shouldFailOnInvalidToken() throws LexerException {
        Lexer lexer = new LexerImpl(source);
        lexer.nextToken();
    }

}
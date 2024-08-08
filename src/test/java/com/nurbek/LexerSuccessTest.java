package com.nurbek;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.fail;

@RunWith(Parameterized.class)
public class LexerSuccessTest {

    private String source;

    public LexerSuccessTest(String source) {
        this.source = source;
    }

    @Parameterized.Parameters(name = "{index} | source=\" {0} \"")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {" < "}, {" > "}, {" >= "}, {" <= "}, {" == "}, {" != "},
                {" = "}, {" + "},  {" - "}, {" / "}, {" * "},
                {"Eq1"},

                {" <df "}, {" >12"}, {" >=var1 "}, {" <=df "}, {" ==23 "}, {" !=var2 "},
                {"25=34 "}, {" +2 "}, {"2-1 "}, {" 2/2 "}, {"2*2"},
                {"qw_1"},
        });
    }

    @Test
    public void shouldFailOnInvalidToken() throws LexerException {
        Lexer lexer = new LexerImpl(source);

        try {
            while (lexer.nextToken().getType() != Token.TokenType.EOF) {}

        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }

}
package com.nurbek;

public class App {
    public static void main(String[] args) {
        String source = """
                PRINT "How many fibonacci numbers do you want?"
                INPUT nums
                                
                LET a = 0
                LET b = 1
                WHILE nums > 0 REPEAT
                    PRINT a
                    LET c = a + b
                    LET a = b
                    LET b = c
                    LET nums = nums - 1
                ENDWHILE
                """;

        Lexer lexer = new LexerImpl(source);

        try {
            Parser parser = new ParserImpl(lexer);

            parser.program();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
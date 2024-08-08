package com.nurbek;

public class App {
    public static void main(String[] args) {
        String source = """
            + - * / == >= <= < >
            23 290 023
            n1
            hello23
            23fd
         """;

        Lexer lexer = new LexerImpl(source);

        try {
            Token token = null;

            while ( (token = lexer.nextToken()).getType() != Token.TokenType.EOF) {
                System.out.println(token.getType() + " " + token.getText());
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
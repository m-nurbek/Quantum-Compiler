package com.nurbek;

public class App {
    public static void main(String[] args) {
        String source = "LET var= var +1\n hello";

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
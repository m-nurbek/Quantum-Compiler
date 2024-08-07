package com.nurbek;

public class App {
    public static void main(String[] args) {
        String source = "HELLO WORLD!";

        Lexer lexer = new LexerImpl(source);

        while (lexer.getCurrChar() != '\0') {
            System.out.println(lexer.getCurrChar());

            lexer.nextChar();
        }


    }
}
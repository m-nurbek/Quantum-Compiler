package com.nurbek;

public class LexerImpl implements Lexer {
    private final String source;
    private int currPos;
    private char currChar;


    public LexerImpl(String source) {
        this.source = source + "\0";
        currPos = -1;
        currChar = '\0';

        nextChar();
    }

    @Override
    public Token nextToken() {
        return null;
    }

    @Override
    public void nextChar() {
        currChar = source.charAt(++currPos);
    }

    @Override
    public char peekChar() {
        if (currPos + 1 < source.length()) {
            return source.charAt(currPos + 1);
        }
        return '\0';
    }

    @Override
    public void abort() {
        System.out.println("Error: aborting lexer");
    }

    @Override
    public void skipWhitespace() {

    }

    @Override
    public void skipComment() {

    }

    @Override
    public char getCurrChar() {
        return currChar;
    }

    @Override
    public int getCurrPos() {
        return currPos;
    }
}
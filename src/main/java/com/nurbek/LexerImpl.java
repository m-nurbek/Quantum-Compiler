package com.nurbek;

import java.util.Arrays;

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
    public Token nextToken() throws LexerException {
        skipComment();
        skipWhitespace();

        Token token = null;

        if (currChar == '\n') {
            token = new TokenImpl("\n", Token.TokenType.NEWLINE);

        } else if (currChar == '\0') {
            token = new TokenImpl("\0", Token.TokenType.EOF);

        } else if (currChar == '+') {

            if (isOperatorEndingValid()) {
                token = new TokenImpl("+", Token.TokenType.PLUS);
            } else {
                abort("Invalid operator '+" + peekChar() + "'");
            }

        } else if (currChar == '-') {

            if (isOperatorEndingValid()) {
                token = new TokenImpl("-", Token.TokenType.MINUS);
            } else {
                abort("Invalid operator '-" + peekChar() + "'");
            }

        } else if (currChar == '/') {

            if (isOperatorEndingValid()) {
                token = new TokenImpl("/", Token.TokenType.SLASH);
            } else {
                abort("Invalid operator '/" + peekChar() + "'");
            }

        } else if (currChar == '*') {

            if (isOperatorEndingValid()) {
                token = new TokenImpl("*", Token.TokenType.ASTERISK);
            } else {
                abort("Invalid operator '*/" + peekChar() + "'");
            }

        } else if (currChar == '=') {

            if (peekChar() == '=') {
                token = new TokenImpl("==", Token.TokenType.EQUALS);
                nextChar();

                if (!(isOperatorEndingValid())) {
                    abort("Invalid operator '==" + peekChar() + "'");
                }
            } else if (isOperatorEndingValid()) {
                token = new TokenImpl("=", Token.TokenType.EQUAL);
            } else {
                abort("Invalid operator '=" + peekChar() + "'");
            }

        } else if (currChar == '>') {

            if (peekChar() == '=') {
                token = new TokenImpl(">=", Token.TokenType.GREATER_EQUAL);
                nextChar();

                if (!(isOperatorEndingValid())) {
                    abort("Invalid operator '>=" + peekChar() + "'");
                }
            } else if (isOperatorEndingValid()) {
                token = new TokenImpl(">", Token.TokenType.GREATER);
            } else {
                abort("Invalid operator '>" + peekChar() + "'");
            }

        } else if (currChar == '<') {

            if (peekChar() == '=') {
                token = new TokenImpl("<=", Token.TokenType.LESS_EQUAL);
                nextChar();

                if (!(isOperatorEndingValid())) {
                    abort("Invalid operator '<=" + peekChar() + "'");
                }
            } else if (isOperatorEndingValid()) {
                token = new TokenImpl("<", Token.TokenType.LESS);
            } else {
                abort("Invalid operator '<" + peekChar() + "'");
            }

        } else if (currChar == '!') {

            if (peekChar() == '=') {
                token = new TokenImpl("!=", Token.TokenType.NOTEQUAL);
                nextChar();

                if (!(isOperatorEndingValid())) {
                    abort("Invalid operator '!=" + peekChar() + "'");
                }
            } else {
                abort("Invalid operator '!" + peekChar() + "'");
            }

        } else if (currChar == '\"') { // string
            token = getStringToken();

        } else if (Character.isDigit(currChar)) { // numbers
            token = getNumberToken();

        } else if (Character.isAlphabetic(currChar) || Character.isDigit(currChar)) { // keywords or identifiers
            token = getKeywordToken();

        } else {
            abort("Invalid Token");
        }

        nextChar();

        return token;
    }

    private boolean isOperatorEndingValid() {
        return Character.isDigit(peekChar()) || Character.isAlphabetic(peekChar()) || Character.isWhitespace(peekChar());
    }

    private Token getStringToken() throws LexerException {
        Token token = null;

        StringBuilder stb = new StringBuilder();
        stb.append('\"');
        nextChar();

        while (currChar != '\"' && currChar != '\0') {
            stb.append(currChar);
            nextChar();
        }

        if (currChar == '\"') {
            stb.append('\"');
        } else {
            abort("Invalid string - no closing quote");
        }

        token = new TokenImpl(stb.toString(), Token.TokenType.STRING);

        return token;
    }

    private Token getNumberToken() throws LexerException {
        Token token = null;

        StringBuilder stb = new StringBuilder();

        while (Character.isDigit(currChar)) {
            stb.append(currChar);
            nextChar();
        }

        if (Character.isAlphabetic(currChar)) {
            abort("Invalid number");
        }

        prevChar();

        token = new TokenImpl(stb.toString(), Token.TokenType.NUMBER);

        return token;
    }

    private Token getKeywordToken() {
        Token token = null;

        StringBuilder stb = new StringBuilder();

        while (Character.isAlphabetic(currChar) || Character.isDigit(currChar) || currChar == '_') {
            stb.append(currChar);
            nextChar();
        }
        prevChar();

        if (Arrays.stream(Token.TokenType.values()).anyMatch(t -> t.name().contentEquals(stb))) {
            token = new TokenImpl(stb.toString(), Token.TokenType.valueOf(stb.toString()));
        } else {
            token = new TokenImpl(stb.toString(), Token.TokenType.IDENTIFIER);
        }

        return token;
    }

    @Override
    public void nextChar() {
        if (currPos + 1 < source.length()) {
            currChar = source.charAt(++currPos);
            return;
        }

        currChar = '\0';
    }

    private void prevChar() {
        if (currPos > 0) {
            currChar = source.charAt(--currPos);
            return;
        }

        currPos = -1;
        currChar = '\0';
    }

    @Override
    public char peekChar() {
        if (currPos + 1 < source.length()) {
            return source.charAt(currPos + 1);
        }
        return '\0';
    }

    @Override
    public void abort(String message) throws LexerException {
        throw new LexerException("ERROR: " + message);
    }

    @Override
    public void skipWhitespace() {
        while (Character.isWhitespace(currChar) && currChar != '\n' && currChar != '\0') {
            nextChar();
        }
    }

    @Override
    public void skipComment() {
        if (currChar == '#') {
            while (currChar != '\n') {
                nextChar();
            }
        }
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
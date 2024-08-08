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
    public Token nextToken() throws LexerException {
        skipComment();
        skipWhitespace();

        Token token = null;
        if (currChar == '\n') {
            token =  new TokenImpl("\n", Token.TokenType.NEWLINE);

        } else if (currChar == '\0') {
            token = new TokenImpl("\0", Token.TokenType.EOF);

        } else if (currChar == '+') {
            token = new TokenImpl("+", Token.TokenType.PLUS);

        } else if (currChar == '-') {
            token = new TokenImpl("-", Token.TokenType.MINUS);

        } else if (currChar == '/') {
            token = new TokenImpl("/", Token.TokenType.SLASH);

        } else if (currChar == '*') {
            token = new TokenImpl("*", Token.TokenType.ASTERISK);

        } else if (currChar == '=') {
            if (peekChar() == '=') {
                token = new TokenImpl("==", Token.TokenType.EQUALS);
                nextChar();
            } else {
                token = new TokenImpl("=", Token.TokenType.EQUAL);
            }

        } else if (currChar == '>') {
            if (peekChar() == '=') {
                token = new TokenImpl(">=", Token.TokenType.GREATER_EQUAL);
                nextChar();
            } else {
                token = new TokenImpl(">", Token.TokenType.GREATER);
            }

        } else if (currChar == '<') {
            if (peekChar() == '=') {
                token = new TokenImpl("<=", Token.TokenType.LESS_EQUAL);
                nextChar();
            } else {
                token =  new TokenImpl("<", Token.TokenType.LESS);
            }

        } else if (Character.isDigit(currChar)) {
            token = getNumberToken();

        } else if (Character.isAlphabetic(currChar) || Character.isDigit(currChar)) {
            token = getKeywordToken();

        } else {
            abort("Invalid Token");
        }

        nextChar();

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

        token = new TokenImpl(stb.toString(), Token.TokenType.NUMBER);

        return token;
    }

    private Token getKeywordToken() {
        Token token = null;

        StringBuilder stb = new StringBuilder();

        while (Character.isAlphabetic(currChar) || Character.isDigit(currChar)) {
            stb.append(currChar);
            nextChar();
        }

        token = new TokenImpl(stb.toString(), Token.TokenType.IDENT);

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
        while (Character.isWhitespace(currChar)) {
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
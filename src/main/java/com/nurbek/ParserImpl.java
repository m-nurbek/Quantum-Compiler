package com.nurbek;

public class ParserImpl implements Parser {
    private final Lexer lexer;
    private Token currToken;
    private Token peekToken;

    public ParserImpl(Lexer lexer) throws LexerException {
        this.lexer = lexer;
        currToken = null;
        peekToken = null;
        nextToken();
        nextToken();
    }

    @Override
    public boolean checkToken(Token.TokenType type) {
        return type == currToken.getType();
    }

    @Override
    public boolean checkPeek(Token.TokenType type) {
        return type == peekToken.getType();
    }

    @Override
    public void match(Token.TokenType type) throws ParserException, LexerException {
        if (!checkToken(type)) {
            abort("Expected Token '" + type + "', but got '" + currToken.getType() + "'");
        }
        nextToken();
    }

    @Override
    public void nextToken() throws LexerException {
        currToken = peekToken;
        peekToken = lexer.nextToken();
    }

    @Override
    public void abort(String message) throws ParserException {
        throw new ParserException("Parser Error: " + message);
    }

    // program ::= {statement}
    public void program() throws LexerException, ParserException {
        System.out.println("--------- PROGRAM ---------");

        // Skip new lines
        while ( checkToken(Token.TokenType.NEWLINE) ) {
            nextToken();
        }

        while ( !checkToken(Token.TokenType.EOF) ) {
            statement();
        }

        System.out.println("---- PARSING COMPLETED ----");
    }

    //  statement ::= "PRINT" (expression | string) nl
    //      | "IF" comparison "THEN" nl {statement} "ENDIF" nl
    //      | "WHILE" comparison "REPEAT" nl {statement} "ENDWHILE" nl
    //      | "LABEL" ident nl
    //      | "GOTO" ident nl
    //      | "LET" ident "=" expression nl
    //      | "INPUT" ident nl
    private void statement() throws LexerException, ParserException {
        System.out.print("STATEMENT ::= ");

        if (checkToken(Token.TokenType.PRINT)) {
            System.out.print("PRINT ");
            nextToken();

            if (checkToken(Token.TokenType.STRING)) {
                System.out.print("STRING (" + currToken.getText() + ") ");
                nextToken();
            } else { // expression
                expression();
            }
        } else if (checkToken(Token.TokenType.IF)) {
            System.out.print("IF ");
            nextToken();

            comparison();

            System.out.print(currToken.getType() + " ");
            match(Token.TokenType.THEN);
            newline();

            while ( !checkToken(Token.TokenType.ENDIF) ) {
                statement();
            }

            System.out.print(currToken.getType() + " ");
            match(Token.TokenType.ENDIF);

        } else if (checkToken(Token.TokenType.WHILE)) {
            System.out.print("WHILE ");
            nextToken();

            comparison();

            System.out.print(currToken.getType() + " ");
            match(Token.TokenType.REPEAT);
            newline();

            while ( !checkToken(Token.TokenType.ENDWHILE) ) {
                statement();
            }

            System.out.print(currToken.getType() + " ");
            match(Token.TokenType.ENDWHILE);

        } else if (checkToken(Token.TokenType.LABEL)) {
            System.out.print("LABEL ");
            nextToken();

            System.out.print("IDENTIFIER (" + currToken.getText() + ") ");
            match(Token.TokenType.IDENTIFIER);

        } else if (checkToken(Token.TokenType.GOTO)) {
            System.out.print("GOTO ");
            nextToken();

            System.out.print("IDENTIFIER (" + currToken.getText() + ") ");
            match(Token.TokenType.IDENTIFIER);

        } else if (checkToken(Token.TokenType.LET)) {
            System.out.print("LET ");
            nextToken();

            System.out.print("IDENTIFIER (" + currToken.getText() + ") ");
            match(Token.TokenType.IDENTIFIER);
            System.out.print("EQUAL (" + currToken.getText() + ") ");
            match(Token.TokenType.EQUAL);

            expression();

        } else if (checkToken(Token.TokenType.INPUT)) {
            System.out.print("INPUT ");
            nextToken();

            System.out.print("IDENTIFIER (" + currToken.getText() + ") ");
            match(Token.TokenType.IDENTIFIER);

        } else {
            abort("Invalid statement at " + currToken.getText() + " (" + currToken.getType() + ")");
        }

        newline();
    }

    // comparison ::= expression (("==" | "!=" | ">" | ">=" | "<" | "<=") expression)+
    private void comparison() throws LexerException, ParserException {
        System.out.print("COMPARISON ");
        expression();

        if (isComparisonOperator()) {
            System.out.print(currToken.getText() + " ");
            nextToken();
            expression();
        } else {
            abort("Expected comparison operator, but got '" + currToken.getType() + "'");
        }

        while (isComparisonOperator()) {
            System.out.print(currToken.getText() + " ");
            nextToken();
            expression();
        }
    }

    private boolean isComparisonOperator() {
        return checkToken(Token.TokenType.EQUALS) || checkToken(Token.TokenType.NOTEQUAL) ||
                checkToken(Token.TokenType.GREATER) || checkToken(Token.TokenType.GREATER_EQUAL) ||
                checkToken(Token.TokenType.LESS) || checkToken(Token.TokenType.LESS_EQUAL);
    }

    // expression ::= term {( "-" | "+" ) term}
    private void expression() throws LexerException, ParserException {
        System.out.print("EXPRESSION ");
        term();

        while (checkToken(Token.TokenType.PLUS) || checkToken(Token.TokenType.MINUS)) {
            System.out.print(currToken.getText() + " ");
            nextToken();
            term();
        }
    }

    // term ::= unary {( "/" | "*" ) unary}
    private void term() throws LexerException, ParserException {
        System.out.print("TERM ");
        unary();

        while (checkToken(Token.TokenType.SLASH) || checkToken(Token.TokenType.ASTERISK)) {
            System.out.print(currToken.getText() + " ");
            nextToken();
            unary();
        }
    }

    // unary ::= ["+" | "-"] primary
    private void unary() throws LexerException, ParserException {
        System.out.print("UNARY ");

        if (checkToken(Token.TokenType.PLUS) || checkToken(Token.TokenType.MINUS)) {
            System.out.print(currToken.getText() + " ");
            nextToken();
        }

        primary();
    }

    // primary ::= number | ident
    private void primary() throws LexerException, ParserException {
        System.out.print("PRIMARY (" + currToken.getText() + ") ");

        if (checkToken(Token.TokenType.NUMBER)) {
            nextToken();
        } else if (checkToken(Token.TokenType.IDENTIFIER)) {
            nextToken();
        } else {
            abort("Expected primary operator, but got '" + currToken.getText() + "'");
        }
    }

    // nl ::= '\n'+
    private void newline() throws LexerException, ParserException {
        System.out.println("NEWLINE");

        match(Token.TokenType.NEWLINE);

        while (checkToken(Token.TokenType.NEWLINE)) {
            nextToken();
        }
    }
}
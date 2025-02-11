package com.nurbek.compiler.component.impl;

import com.nurbek.compiler.component.Emitter;
import com.nurbek.compiler.component.Lexer;
import com.nurbek.compiler.component.Parser;
import com.nurbek.compiler.component.Token;
import com.nurbek.compiler.exception.LexerException;
import com.nurbek.compiler.exception.ParserException;

public class ParserImpl implements Parser {
    private Lexer lexer;
    private Emitter emitter;
    private Token currToken;
    private Token peekToken;

    private int currentTabSize = 0;

    private String appendTabs() {
        return "\t".repeat(Math.max(0, currentTabSize));
    }

    @Override
    public boolean checkToken(Token.TokenType type) {
        return type == currToken.type();
    }

    @Override
    public boolean checkPeek(Token.TokenType type) {
        return type == peekToken.type();
    }

    @Override
    public void match(Token.TokenType type) throws ParserException, LexerException {
        if (!checkToken(type)) {
            abort("Expected Token '" + type + "', but got '" + currToken.type() + "'");
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
        while (checkToken(Token.TokenType.NEWLINE)) {
            nextToken();
        }

        while (!checkToken(Token.TokenType.EOF)) {
            statement();
        }

        System.out.println("---- PARSING COMPLETED ----");
    }

    @Override
    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
        currToken = null;
        peekToken = null;
        nextToken();
        nextToken();
    }

    @Override
    public void setEmitter(Emitter emitter) {
        this.emitter = emitter;
    }

    //  statement ::= "PRINT" (expression | string) nl
    //      | "IF" comparison "THEN" nl {statement} "ENDIF" nl
    //      | "WHILE" comparison "REPEAT" nl {statement} "ENDWHILE" nl
    //      | "LET" ident "=" expression nl
    //      | "INPUT" ident nl
    private void statement() throws LexerException, ParserException {
        System.out.print("STATEMENT ::= ");

        if (checkToken(Token.TokenType.PRINT)) {
            System.out.print("PRINT ");
            nextToken();

            if (checkToken(Token.TokenType.STRING)) {
                System.out.print("STRING (" + currToken.text() + ") ");

                emitter.emitLine(appendTabs() + "print(" + currToken.text() + ")");
                nextToken();
            } else { // expression
                emitter.emit(appendTabs() + "print(");
                expression();
                emitter.emitLine(")");
            }
        } else if (checkToken(Token.TokenType.IF)) {
            System.out.print("IF ");
            nextToken();

            emitter.emit(appendTabs() + "if ");

            comparison();

            System.out.print(currToken.type() + " ");
            match(Token.TokenType.THEN);
            newline();

            emitter.emitLine(":");
            currentTabSize++;

            while (!checkToken(Token.TokenType.ENDIF)) {
                statement();
            }

            System.out.print(currToken.type() + " ");
            match(Token.TokenType.ENDIF);

            emitter.emitLine("");
            currentTabSize--;

        } else if (checkToken(Token.TokenType.WHILE)) {
            System.out.print("WHILE ");
            nextToken();

            emitter.emit(appendTabs() + "while ");

            comparison();

            System.out.print(currToken.type() + " ");
            match(Token.TokenType.REPEAT);
            newline();

            emitter.emitLine(":");
            currentTabSize++;

            while (!checkToken(Token.TokenType.ENDWHILE)) {
                statement();
            }

            System.out.print(currToken.type() + " ");
            match(Token.TokenType.ENDWHILE);

            emitter.emitLine("");
            currentTabSize--;

        } else if (checkToken(Token.TokenType.LET)) {
            System.out.print("LET ");
            nextToken();

            emitter.emit(appendTabs() + currToken.text() + " = ");
            System.out.print("IDENTIFIER (" + currToken.text() + ") ");
            match(Token.TokenType.IDENTIFIER);
            System.out.print("EQUAL (" + currToken.text() + ") ");
            match(Token.TokenType.EQUAL);

            expression();
            emitter.emitLine("");

        } else if (checkToken(Token.TokenType.INPUT)) {
            System.out.print("INPUT ");
            nextToken();

            System.out.print("IDENTIFIER (" + currToken.text() + ") ");
            emitter.emitLine(appendTabs() + currToken.text() + " = int(input())");
            match(Token.TokenType.IDENTIFIER);
        } else {
            abort("Invalid statement at " + currToken.text() + " (" + currToken.type() + ")");
        }

        newline();
    }

    // comparison ::= expression (("==" | "!=" | ">" | ">=" | "<" | "<=") expression)+
    private void comparison() throws LexerException, ParserException {
        System.out.print("COMPARISON ");
        expression();

        if (isComparisonOperator()) {
            System.out.print(currToken.text() + " ");

            emitter.emit(currToken.text());
            nextToken();
            expression();
        } else {
            abort("Expected comparison operator, but got '" + currToken.type() + "'");
        }

        while (isComparisonOperator()) {
            System.out.print(currToken.text() + " ");
            emitter.emit(currToken.text());
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
            System.out.print(currToken.text() + " ");
            emitter.emit(currToken.text());
            nextToken();
            term();
        }
    }

    // term ::= unary {( "/" | "*" ) unary}
    private void term() throws LexerException, ParserException {
        System.out.print("TERM ");
        unary();

        while (checkToken(Token.TokenType.SLASH) || checkToken(Token.TokenType.ASTERISK)) {
            System.out.print(currToken.text() + " ");
            emitter.emit(currToken.text());
            nextToken();
            unary();
        }
    }

    // unary ::= ["+" | "-"] primary
    private void unary() throws LexerException, ParserException {
        System.out.print("UNARY ");

        if (checkToken(Token.TokenType.PLUS) || checkToken(Token.TokenType.MINUS)) {
            System.out.print(currToken.text() + " ");
            emitter.emit(currToken.text());
            nextToken();
        }

        primary();
    }

    // primary ::= number | ident
    private void primary() throws LexerException, ParserException {
        System.out.print("PRIMARY (" + currToken.text() + ") ");
        emitter.emit(currToken.text());

        if (checkToken(Token.TokenType.NUMBER)) {
            nextToken();
        } else if (checkToken(Token.TokenType.IDENTIFIER)) {
            nextToken();
        } else {
            abort("Expected primary operator, but got '" + currToken.text() + "'");
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
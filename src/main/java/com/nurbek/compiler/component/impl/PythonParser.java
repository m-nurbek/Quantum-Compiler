package com.nurbek.compiler.component.impl;

import com.nurbek.compiler.Application;
import com.nurbek.compiler.component.Emitter;
import com.nurbek.compiler.component.Lexer;
import com.nurbek.compiler.component.Parser;
import com.nurbek.compiler.component.Token;
import com.nurbek.compiler.exception.LexerException;
import com.nurbek.compiler.exception.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PythonParser implements Parser {
    private Lexer lexer;
    private Emitter emitter;
    private Token currToken;
    private Token peekToken;
    private int currentTabSize = 0;

    // output will be used in logging
    private final StringBuilder output = new StringBuilder();
    private static final Logger log = LoggerFactory.getLogger(PythonParser.class);

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
        log.debug("--------- PROGRAM ---------");

        // Skip new lines
        while (checkToken(Token.TokenType.NEWLINE)) {
            nextToken();
        }

        while (!checkToken(Token.TokenType.EOF)) {
            statement();
        }

        log.debug("\n\n" + output);
        log.debug("---- PARSING COMPLETED ----");
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
        output.append("STATEMENT ::= ");

        if (checkToken(Token.TokenType.PRINT)) {
            output.append("PRINT ");
            nextToken();

            if (checkToken(Token.TokenType.STRING)) {
                output.append("STRING (" + currToken.text() + ") ");

                emitter.emitLine(appendTabs() + "print(" + currToken.text() + ")");
                nextToken();
            } else { // expression
                emitter.emit(appendTabs() + "print(");
                expression();
                emitter.emitLine(")");
            }
        } else if (checkToken(Token.TokenType.IF)) {
            output.append("IF ");
            nextToken();

            emitter.emit(appendTabs() + "if ");

            comparison();

            output.append(currToken.type() + " ");
            match(Token.TokenType.THEN);
            newline();

            emitter.emitLine(":");
            currentTabSize++;

            while (!checkToken(Token.TokenType.ENDIF)) {
                statement();
            }

            output.append(currToken.type() + " ");
            match(Token.TokenType.ENDIF);

            emitter.emitLine("");
            currentTabSize--;

        } else if (checkToken(Token.TokenType.WHILE)) {
            output.append("WHILE ");
            nextToken();

            emitter.emit(appendTabs() + "while ");

            comparison();

            output.append(currToken.type() + " ");
            match(Token.TokenType.REPEAT);
            newline();

            emitter.emitLine(":");
            currentTabSize++;

            while (!checkToken(Token.TokenType.ENDWHILE)) {
                statement();
            }

            output.append(currToken.type() + " ");
            match(Token.TokenType.ENDWHILE);

            emitter.emitLine("");
            currentTabSize--;

        } else if (checkToken(Token.TokenType.LET)) {
            output.append("LET ");
            nextToken();

            emitter.emit(appendTabs() + currToken.text() + " = ");
            output.append("IDENTIFIER (" + currToken.text() + ") ");
            match(Token.TokenType.IDENTIFIER);
            output.append("EQUAL (" + currToken.text() + ") ");
            match(Token.TokenType.EQUAL);

            expression();
            emitter.emitLine("");

        } else if (checkToken(Token.TokenType.INPUT)) {
            output.append("INPUT ");
            nextToken();

            output.append("IDENTIFIER (" + currToken.text() + ") ");
            emitter.emitLine(appendTabs() + currToken.text() + " = int(input())");
            match(Token.TokenType.IDENTIFIER);
        } else {
            abort("Invalid statement at " + currToken.text() + " (" + currToken.type() + ")");
        }

        newline();
    }

    // comparison ::= expression (("==" | "!=" | ">" | ">=" | "<" | "<=") expression)+
    private void comparison() throws LexerException, ParserException {
        output.append("COMPARISON ");
        expression();

        if (isComparisonOperator()) {
            output.append(currToken.text() + " ");

            emitter.emit(currToken.text());
            nextToken();
            expression();
        } else {
            abort("Expected comparison operator, but got '" + currToken.type() + "'");
        }

        while (isComparisonOperator()) {
            output.append(currToken.text() + " ");
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
        output.append("EXPRESSION ");
        term();

        while (checkToken(Token.TokenType.PLUS) || checkToken(Token.TokenType.MINUS)) {
            output.append(currToken.text() + " ");
            emitter.emit(currToken.text());
            nextToken();
            term();
        }
    }

    // term ::= unary {( "/" | "*" ) unary}
    private void term() throws LexerException, ParserException {
        output.append("TERM ");
        unary();

        while (checkToken(Token.TokenType.SLASH) || checkToken(Token.TokenType.ASTERISK)) {
            output.append(currToken.text() + " ");
            emitter.emit(currToken.text());
            nextToken();
            unary();
        }
    }

    // unary ::= ["+" | "-"] primary
    private void unary() throws LexerException, ParserException {
        output.append("UNARY ");

        if (checkToken(Token.TokenType.PLUS) || checkToken(Token.TokenType.MINUS)) {
            output.append(currToken.text() + " ");
            emitter.emit(currToken.text());
            nextToken();
        }

        primary();
    }

    // primary ::= number | ident
    private void primary() throws LexerException, ParserException {
        output.append("PRIMARY (" + currToken.text() + ") ");
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
        output.append("NEWLINE\n");
        match(Token.TokenType.NEWLINE);

        while (checkToken(Token.TokenType.NEWLINE)) {
            nextToken();
        }
    }
}
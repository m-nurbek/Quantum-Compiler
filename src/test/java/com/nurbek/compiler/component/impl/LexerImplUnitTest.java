package com.nurbek.compiler.component.impl;

import com.nurbek.compiler.component.Lexer;
import com.nurbek.compiler.component.Token;
import com.nurbek.compiler.exception.LexerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LexerImplUnitTest {

    @Test
    public void shouldCorrectlySplitCharacters() {
        String source = """
                Hello World
                
                How are you?
                """;

        Lexer lexer = new LexerImpl();
        lexer.setSource(source);

        List<Character> list = new LinkedList<>();

        while (lexer.getCurrChar() != '\0') {
            list.add(lexer.getCurrChar());
            lexer.nextChar();
        }

        List<Character> actual = new LinkedList<>();

        for (char c : source.toCharArray()) {
            actual.add(c);
        }

        assertThat(list).isEqualTo(actual);
    }

    @Test
    public void shouldCorrectlyTokenizeOperators() {
        String source = """
                + - = / < > == <= >= !=
                """;

        List<Token.TokenType> list = new ArrayList<>();
        list.add(Token.TokenType.PLUS);
        list.add(Token.TokenType.MINUS);
        list.add(Token.TokenType.EQUAL);
        list.add(Token.TokenType.SLASH);
        list.add(Token.TokenType.LESS);
        list.add(Token.TokenType.GREATER);
        list.add(Token.TokenType.EQUALS);
        list.add(Token.TokenType.LESS_EQUAL);
        list.add(Token.TokenType.GREATER_EQUAL);
        list.add(Token.TokenType.NOTEQUAL);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.EOF);

        Lexer lexer = new LexerImpl();
        lexer.setSource(source);

        Token token = null;

        try {
            int i = 0;

            while ((token = lexer.nextToken()).type() != Token.TokenType.EOF) {
                assertThat(token.type()).isEqualTo(list.get(i));
                i++;
            }
        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }

    @Test
    public void shouldCorrectlySplitTokens() {
        String source = """
                LET var=var+223
                """;

        Lexer lexer = new LexerImpl();
        lexer.setSource(source);

        List<Token.TokenType> list = new ArrayList<>();
        list.add(Token.TokenType.LET);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.EQUAL);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.PLUS);
        list.add(Token.TokenType.NUMBER);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.EOF);

        Token token = null;

        try {
            int i = 0;

            while ((token = lexer.nextToken()).type() != Token.TokenType.EOF) {
                assertThat(token.type()).isEqualTo(list.get(i));
                i++;
            }
        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }

    @Test
    public void shouldCorrectlySplitTokens2() {
        String source = """
                PRINT \"How many fibonacci numbers do you want?\"
                INPUT nums
                
                LET a= 0
                LET b =1
                
                WHILE nums>=0 REPEAT
                    PRINT a
                    LET c = a + b
                    LET a = b
                    LET b = c
                    LET nums = nums-1      
                ENDWHILE
                
                """;

        Lexer lexer = new LexerImpl();
        lexer.setSource(source);

        List<Token.TokenType> list = new ArrayList<>();
        list.add(Token.TokenType.PRINT);
        list.add(Token.TokenType.STRING);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.INPUT);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.LET);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.EQUAL);
        list.add(Token.TokenType.NUMBER);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.LET);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.EQUAL);
        list.add(Token.TokenType.NUMBER);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.WHILE);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.GREATER_EQUAL);
        list.add(Token.TokenType.NUMBER);
        list.add(Token.TokenType.REPEAT);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.PRINT);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.LET);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.EQUAL);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.PLUS);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.LET);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.EQUAL);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.LET);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.EQUAL);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.LET);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.EQUAL);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.MINUS);
        list.add(Token.TokenType.NUMBER);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.ENDWHILE);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.NEWLINE);
        list.add(Token.TokenType.EOF);

        Token token = null;

        try {
            int i = 0;

            while ((token = lexer.nextToken()).type() != Token.TokenType.EOF) {
                assertThat(token.type()).isEqualTo(list.get(i));
                i++;
            }

        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }

    // SUCCESS UNIT TESTS
    private static Stream<String> validData() {
        return Stream.of(
                " < ", " > ", " >= ", " <= ", " == ", " != ",
                " = ", " + ", " - ", " / ", " * ", "Eq1",
                " <df ", " >12", " >=var1 ", " <=df ", " ==23 ", " !=var2 ",
                "25=34 ", " +2 ", "2-1 ", " 2/2 ", "2*2", "qw_1"
        );
    }

    @ParameterizedTest
    @MethodSource("validData")
    void shouldNotFailOnInvalidToken(String source) {
        Lexer lexer = new LexerImpl();
        lexer.setSource(source);

        try {
            while (lexer.nextToken().type() != Token.TokenType.EOF) {
            }
        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }

    // FAIL UNIT TESTS
    private static Stream<String> invalidData() {
        return Stream.of("<<", ">>=", "=!", "1ewq", "+-", "-+", "===", "!!=", "!=");
    }

    @ParameterizedTest
    @MethodSource("invalidData")
    public void shouldFailOnInvalidToken(String source) throws LexerException {
        assertThrows(LexerException.class, () -> {
            Lexer lexer = new LexerImpl();
            lexer.setSource(source);
            lexer.nextToken();
        });
    }
}
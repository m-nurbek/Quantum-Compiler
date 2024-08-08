package com.nurbek;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class LexerTest {

    @Test
    public void shouldCorrectlySplitCharacters() {
        String source = """
                Hello World
                
                How are you?
                """;

        Lexer lexer = new LexerImpl(source);

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

        Lexer lexer = new LexerImpl(source);

        Token token = null;

        try {
            int i = 0;

            while ((token = lexer.nextToken()).getType() != Token.TokenType.EOF) {
                assertThat(token.getType()).isEqualTo(list.get(i));
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

        Lexer lexer = new LexerImpl(source);

        List<Token.TokenType> list = new ArrayList<>();
        list.add(Token.TokenType.LET);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.EQUAL);
        list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.PLUS);
        list.add(Token.TokenType.NUMBER);

        Token token = null;

        try {
            int i = 0;

            while ((token = lexer.nextToken()).getType() != Token.TokenType.EOF) {
                assertThat(token.getType()).isEqualTo(list.get(i));
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

        Lexer lexer = new LexerImpl(source);

        List<Token.TokenType> list = new ArrayList<>();
        list.add(Token.TokenType.PRINT); list.add(Token.TokenType.STRING);
        list.add(Token.TokenType.INPUT); list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.LET); list.add(Token.TokenType.IDENTIFIER); list.add(Token.TokenType.EQUAL); list.add(Token.TokenType.NUMBER);
        list.add(Token.TokenType.LET); list.add(Token.TokenType.IDENTIFIER); list.add(Token.TokenType.EQUAL); list.add(Token.TokenType.NUMBER);
        list.add(Token.TokenType.WHILE); list.add(Token.TokenType.IDENTIFIER); list.add(Token.TokenType.GREATER_EQUAL); list.add(Token.TokenType.NUMBER); list.add(Token.TokenType.REPEAT);
        list.add(Token.TokenType.PRINT); list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.LET); list.add(Token.TokenType.IDENTIFIER); list.add(Token.TokenType.EQUAL); list.add(Token.TokenType.IDENTIFIER); list.add(Token.TokenType.PLUS); list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.LET); list.add(Token.TokenType.IDENTIFIER); list.add(Token.TokenType.EQUAL); list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.LET); list.add(Token.TokenType.IDENTIFIER); list.add(Token.TokenType.EQUAL); list.add(Token.TokenType.IDENTIFIER);
        list.add(Token.TokenType.LET); list.add(Token.TokenType.IDENTIFIER); list.add(Token.TokenType.EQUAL); list.add(Token.TokenType.IDENTIFIER); list.add(Token.TokenType.MINUS);list.add(Token.TokenType.NUMBER);
        list.add(Token.TokenType.ENDWHILE);

        Token token = null;

        try {
            int i = 0;

            while ((token = lexer.nextToken()).getType() != Token.TokenType.EOF) {
                assertThat(token.getType()).isEqualTo(list.get(i));
                i++;
            }

        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }

}
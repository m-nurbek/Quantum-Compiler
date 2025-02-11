package com.nurbek.compiler.component;

import com.nurbek.compiler.component.Token.TokenType;
import com.nurbek.compiler.component.impl.LexerImpl;
import com.nurbek.compiler.exception.LexerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static com.nurbek.compiler.component.Token.TokenType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LexerUnitTest {

    private Lexer lexer;

    @BeforeEach
    void setup() {
        lexer = new LexerImpl();
    }

    @Test
    public void shouldNotBeNullAfterInitialization() {
        assertNotNull(lexer);
    }

    @Test
    public void shouldNotBeEmptyAfterSettingSource() {
        // given
        lexer.setSource("some code");

        // then
        assertFalse(lexer.getCurrChar() == '\0');
    }

    private static Stream<String> validSyntax() {
        return Stream.of(
                " < ", " > ", " >= ", " <= ", " == ", " != ",
                " = ", " + ", " - ", " / ", " * ", "Eq1",
                " <df ", " >12", " >=var1 ", " <=df ", " ==23 ", " !=var2 ",
                "25=34 ", " +2 ", "2-1 ", " 2/2 ", "2*2", "qw_1"
        );
    }

    @ParameterizedTest
    @MethodSource("validSyntax")
    void shouldNotFailOnInvalidToken(String source) {
        lexer.setSource(source);

        try {
            while (lexer.nextToken().type() != EOF) {
            }
        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }

    private static Stream<String> invalidSyntax() {
        return Stream.of("<<", ">>=", "=!", "1ewq", "+-", "-+", "===", "!!=", "!=");
    }

    @ParameterizedTest
    @MethodSource("invalidSyntax")
    public void shouldFailOnInvalidToken(String source) {
        assertThrows(LexerException.class, () -> {
            lexer.setSource(source);
            lexer.nextToken();
        });
    }

    @Test
    public void shouldCorrectlySplitCharacters() {
        // given
        String source = """
                Hello World
                
                How are you?
                """;
        lexer.setSource(source);
        List<Character> actual = source.chars().mapToObj(c -> (char) c).toList();

        // when
        List<Character> list = new LinkedList<>();
        while (lexer.getCurrChar() != '\0') {
            list.add(lexer.getCurrChar());
            lexer.nextChar();
        }

        // then
        assertThat(list).isEqualTo(actual);
    }

    private static Stream<Arguments> sourceAndTokens() {
        return Stream.of(
                Arguments.of("""
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
                                
                                """,
                        List.of(
                                PRINT, STRING, NEWLINE,
                                INPUT, IDENTIFIER, NEWLINE, NEWLINE,
                                LET, IDENTIFIER, EQUAL, NUMBER, NEWLINE,
                                LET, IDENTIFIER, EQUAL, NUMBER, NEWLINE, NEWLINE,
                                WHILE, IDENTIFIER, GREATER_EQUAL, NUMBER, REPEAT, NEWLINE,
                                PRINT, IDENTIFIER, NEWLINE,
                                LET, IDENTIFIER, EQUAL, IDENTIFIER, PLUS, IDENTIFIER, NEWLINE,
                                LET, IDENTIFIER, EQUAL, IDENTIFIER, NEWLINE,
                                LET, IDENTIFIER, EQUAL, IDENTIFIER, NEWLINE,
                                LET, IDENTIFIER, EQUAL, IDENTIFIER, MINUS, NUMBER, NEWLINE,
                                ENDWHILE, NEWLINE, NEWLINE, EOF)
                ),
                Arguments.of("""
                                LET var=var+223
                                """,
                        List.of(LET, IDENTIFIER, EQUAL, IDENTIFIER, PLUS, NUMBER, NEWLINE, EOF)
                ),
                Arguments.of("""
                                + - = / < > == <= >= !=
                                """,
                        List.of(
                                PLUS, MINUS, EQUAL, SLASH, LESS, GREATER, EQUALS,
                                LESS_EQUAL, GREATER_EQUAL, NOTEQUAL, NEWLINE, EOF)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("sourceAndTokens")
    public void shouldCorrectlySplitTokens(String source, List<TokenType> listOfTokens) {
        lexer.setSource(source);
        Token token;

        try {
            int i = 0;

            while ((token = lexer.nextToken()).type() != EOF) {
                assertThat(token.type()).isEqualTo(listOfTokens.get(i));
                i++;
            }

        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }

    @RepeatedTest(5)
    void repeatableTest() {
        // given
        lexer.setSource("source code");

        // when
        lexer.nextChar();
        char currentChar = lexer.getCurrChar();

        // then
        assertThat(currentChar).isEqualTo('o');
        assertThat(lexer.getCurrPos()).isEqualTo(1);
        assertThat(lexer.peekChar()).isEqualTo('u');
    }

    @Test
    public void shouldThrowExceptionOnNullSource() {
        assertThrows(NullPointerException.class, () -> {
            lexer.setSource(null);
            lexer.nextToken();
        });
    }

    @Test
    public void shouldThrowExceptionOnEmptySource() {
        lexer.setSource("");
        Token token = lexer.nextToken();
        assertThat(token.type()).isEqualTo(EOF);
    }

    @Test
    public void shouldThrowExceptionOnUnrecognizedCharacter() {
        lexer.setSource("@");
        assertThrows(LexerException.class, () -> lexer.nextToken());
    }

    @Test
    public void shouldThrowExceptionOnUnclosedString() {
        lexer.setSource("\"unclosed string");
        assertThrows(LexerException.class, () -> lexer.nextToken());
    }

    @Test
    public void shouldThrowExceptionOnInvalidNumberFormat() {
        lexer.setSource("123abc");
        assertThrows(LexerException.class, () -> lexer.nextToken());
    }
}
package interpreter.lexer;


import interpreter.token.Token;
import interpreter.token.TokenType;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LexerTest {

    @Test
    void testNextToken() {
        String input = "=+(){},;";

        List<Pair<TokenType, String>> expectedResults = List.of(
                Pair.of(TokenType.ASSIGN, "="),
                Pair.of(TokenType.PLUS, "+"),
                Pair.of(TokenType.LPAREN, "("),
                Pair.of(TokenType.RPAREN, ")"),
                Pair.of(TokenType.LBRACE, "{"),
                Pair.of(TokenType.RBRACE, "}"),
                Pair.of(TokenType.COMMA, ","),
                Pair.of(TokenType.SEMICOLON, ";"),
                Pair.of(TokenType.EOF, String.valueOf('\0'))
        );

        Lexer lexer = new Lexer(input);

        expectedResults.forEach(pair -> {
            Token actual = lexer.nextToken();

            assertThat(actual.type()).isEqualTo(pair.getLeft());
            assertThat(actual.literal()).isEqualTo(pair.getRight());
        });
    }

}

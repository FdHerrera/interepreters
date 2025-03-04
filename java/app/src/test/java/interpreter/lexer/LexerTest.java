package interpreter.lexer;


import interpreter.token.Token;
import interpreter.token.TokenType;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static interpreter.token.TokenType.ASSIGN;
import static interpreter.token.TokenType.COMMA;
import static interpreter.token.TokenType.FUNCTION;
import static interpreter.token.TokenType.IDENT;
import static interpreter.token.TokenType.INT;
import static interpreter.token.TokenType.LBRACE;
import static interpreter.token.TokenType.LET;
import static interpreter.token.TokenType.LPAREN;
import static interpreter.token.TokenType.PLUS;
import static interpreter.token.TokenType.RBRACE;
import static interpreter.token.TokenType.RPAREN;
import static interpreter.token.TokenType.SEMICOLON;
import static org.assertj.core.api.Assertions.assertThat;

class LexerTest {

    @Test
    void testNextToken() {
        String input = """
                let five = 5;
                let ten = 10;
                let add = fn(x, y) {
                  x + y;
                };
                let result = add(five, ten);
                """;

        List<Pair<TokenType, String>> expectedResults = List.of(
                Pair.of(LET, "let"),
                Pair.of(IDENT, "five"),
                Pair.of(ASSIGN, "="),
                Pair.of(INT, "5"),
                Pair.of(SEMICOLON, ";"),
                Pair.of(LET, "let"),
                Pair.of(IDENT, "ten"),
                Pair.of(ASSIGN, "="),
                Pair.of(INT, "10"),
                Pair.of(SEMICOLON, ";"),
                Pair.of(LET, "let"),
                Pair.of(IDENT, "add"),
                Pair.of(ASSIGN, "="),
                Pair.of(FUNCTION, "fn"),
                Pair.of(LPAREN, "("),
                Pair.of(IDENT, "x"),
                Pair.of(COMMA, ","),
                Pair.of(IDENT, "y"),
                Pair.of(RPAREN, ")"),
                Pair.of(LBRACE, "{"),
                Pair.of(IDENT, "x"),
                Pair.of(PLUS, "+"),
                Pair.of(IDENT, "y"),
                Pair.of(SEMICOLON, ";"),
                Pair.of(RBRACE, "}"),
                Pair.of(SEMICOLON, ";"),
                Pair.of(LET, "let"),
                Pair.of(IDENT, "result"),
                Pair.of(ASSIGN, "="),
                Pair.of(IDENT, "add"),
                Pair.of(LPAREN, "("),
                Pair.of(IDENT, "five"),
                Pair.of(COMMA, ","),
                Pair.of(IDENT, "ten"),
                Pair.of(RPAREN, ")"),
                Pair.of(SEMICOLON, ";"),
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

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
        String input = """
                let five = 5;
                let ten = 10;
                
                let add = fn(x, y) {
                  x + y;
                };
                
                let result = add(five, ten);
                !-/*5;
                5 < 10 > 5;
                
                if (5 < 10) {
                	return true;
                } else {
                	return false;
                }
                """;
//
//        10 == 10;
//        10 != 9;
//        """;

        List<Pair<TokenType, String>> expectedResults = List.<Pair<TokenType, String>>of(
                Pair.of(TokenType.LET, "let"),
                Pair.of(TokenType.IDENT, "five"),
                Pair.of(TokenType.ASSIGN, "="),
                Pair.of(TokenType.INT, "5"),
                Pair.of(TokenType.SEMICOLON, ";"),
                Pair.of(TokenType.LET, "let"),
                Pair.of(TokenType.IDENT, "ten"),
                Pair.of(TokenType.ASSIGN, "="),
                Pair.of(TokenType.INT, "10"),
                Pair.of(TokenType.SEMICOLON, ";"),
                Pair.of(TokenType.LET, "let"),
                Pair.of(TokenType.IDENT, "add"),
                Pair.of(TokenType.ASSIGN, "="),
                Pair.of(TokenType.FUNCTION, "fn"),
                Pair.of(TokenType.LPAREN, "("),
                Pair.of(TokenType.IDENT, "x"),
                Pair.of(TokenType.COMMA, ","),
                Pair.of(TokenType.IDENT, "y"),
                Pair.of(TokenType.RPAREN, ")"),
                Pair.of(TokenType.LBRACE, "{"),
                Pair.of(TokenType.IDENT, "x"),
                Pair.of(TokenType.PLUS, "+"),
                Pair.of(TokenType.IDENT, "y"),
                Pair.of(TokenType.SEMICOLON, ";"),
                Pair.of(TokenType.RBRACE, "}"),
                Pair.of(TokenType.SEMICOLON, ";"),
                Pair.of(TokenType.LET, "let"),
                Pair.of(TokenType.IDENT, "result"),
                Pair.of(TokenType.ASSIGN, "="),
                Pair.of(TokenType.IDENT, "add"),
                Pair.of(TokenType.LPAREN, "("),
                Pair.of(TokenType.IDENT, "five"),
                Pair.of(TokenType.COMMA, ","),
                Pair.of(TokenType.IDENT, "ten"),
                Pair.of(TokenType.RPAREN, ")"),
                Pair.of(TokenType.SEMICOLON, ";"),
                Pair.of(TokenType.BANG, "!"),
                Pair.of(TokenType.MINUS, "-"),
                Pair.of(TokenType.SLASH, "/"),
                Pair.of(TokenType.ASTERISK, "*"),
                Pair.of(TokenType.INT, "5"),
                Pair.of(TokenType.SEMICOLON, ";"),
                Pair.of(TokenType.INT, "5"),
                Pair.of(TokenType.LT, "<"),
                Pair.of(TokenType.INT, "10"),
                Pair.of(TokenType.GT, ">"),
                Pair.of(TokenType.INT, "5"),
                Pair.of(TokenType.SEMICOLON, ";"),
                Pair.of(TokenType.IF, "if"),
                Pair.of(TokenType.LPAREN, "("),
                Pair.of(TokenType.INT, "5"),
                Pair.of(TokenType.LT, "<"),
                Pair.of(TokenType.INT, "10"),
                Pair.of(TokenType.RPAREN, ")"),
                Pair.of(TokenType.LBRACE, "{"),
                Pair.of(TokenType.RETURN, "return"),
                Pair.of(TokenType.TRUE, "true"),
                Pair.of(TokenType.SEMICOLON, ";"),
                Pair.of(TokenType.RBRACE, "}"),
                Pair.of(TokenType.ELSE, "else"),
                Pair.of(TokenType.LBRACE, "{"),
                Pair.of(TokenType.RETURN, "return"),
                Pair.of(TokenType.FALSE, "false"),
                Pair.of(TokenType.SEMICOLON, ";"),
                Pair.of(TokenType.RBRACE, "}")
//        Pair.of(TokenType.INT, "10"),
//        Pair.of(TokenType.EQ, "=="),
//        Pair.of(TokenType.INT, "10"),
//        Pair.of(TokenType.SEMICOLON, ";"),
//        Pair.of(TokenType.INT, "10"),
//        Pair.of(TokenType.NOT_EQ, "!="),
//        Pair.of(TokenType.INT, "9"),
//        Pair.of(TokenType.SEMICOLON, ";"),
//        Pair.of(TokenType.EOF, "")
        );

        Lexer lexer = new Lexer(input);

        expectedResults.forEach(pair -> {
            Token actual = lexer.nextToken();

            assertThat(actual.type()).isEqualTo(pair.getLeft());
            assertThat(actual.literal()).isEqualTo(pair.getRight());
        });
    }

}

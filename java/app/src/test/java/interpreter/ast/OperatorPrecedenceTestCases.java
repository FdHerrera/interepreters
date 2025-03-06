package interpreter.ast;

import interpreter.token.Token;
import interpreter.token.TokenType;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.params.provider.Arguments;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OperatorPrecedenceTestCases {
    private static final Token MINUS_TOKEN = new Token(TokenType.MINUS, "-");
    private static final Token PLUS_TOKEN = new Token(TokenType.PLUS, "+");
    private static final Token TIMES_TOKEN = new Token(TokenType.ASTERISK, "*");
    private static final Token SLASH_TOKEN = new Token(TokenType.SLASH, "/");

    static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(
                        "-a * b",
                        List.of(
                                new ExpressionStatement(
                                        identifierTokenOf("b"),
                                        new InfixExpression(
                                                new PrefixExpression(
                                                        MINUS_TOKEN, identifierOf("a")),
                                                TIMES_TOKEN,
                                                identifierOf("b"))))),
                Arguments.of(
                        "!-a",
                        List.of(
                                new ExpressionStatement(
                                        identifierTokenOf("a"),
                                        new PrefixExpression(
                                                new Token(TokenType.BANG, "!"),
                                                new PrefixExpression(
                                                        MINUS_TOKEN, identifierOf("a")))))),
                Arguments.of(
                        "a + b + c",
                        List.of(
                                new ExpressionStatement(
                                        identifierTokenOf("c"),
                                        new InfixExpression(
                                                new InfixExpression(
                                                        identifierOf("a"),
                                                        PLUS_TOKEN,
                                                        identifierOf("b")),
                                                PLUS_TOKEN,
                                                identifierOf("c"))))),
                Arguments.of(
                        "a + b - c",
                        List.of(
                                new ExpressionStatement(
                                        identifierTokenOf("c"),
                                        new InfixExpression(
                                                new InfixExpression(
                                                        identifierOf("a"),
                                                        PLUS_TOKEN,
                                                        identifierOf("b")),
                                                MINUS_TOKEN,
                                                identifierOf("c"))))),
                Arguments.of(
                        "a * b * c",
                        List.of(
                                new ExpressionStatement(
                                        identifierTokenOf("c"),
                                        new InfixExpression(
                                                new InfixExpression(
                                                        identifierOf("a"),
                                                        TIMES_TOKEN,
                                                        identifierOf("b")),
                                                TIMES_TOKEN,
                                                identifierOf("c")))),
                        Arguments.of(
                                "a * b / c",
                                new ExpressionStatement(
                                        identifierTokenOf("c"),
                                        new InfixExpression(
                                                new InfixExpression(
                                                        identifierOf("a"),
                                                        TIMES_TOKEN,
                                                        identifierOf("b")),
                                                SLASH_TOKEN,
                                                identifierOf("c"))))),
                Arguments.of(
                        "a + b / c",
                        List.of(
                                new ExpressionStatement(
                                        identifierTokenOf("c"),
                                        new InfixExpression(
                                                identifierOf("a"),
                                                PLUS_TOKEN,
                                                new InfixExpression(
                                                        identifierOf("b"),
                                                        SLASH_TOKEN,
                                                        identifierOf("c")))))),
                Arguments.of(
                        "a + b * c",
                        List.of(
                                new ExpressionStatement(
                                        identifierTokenOf("c"),
                                        new InfixExpression(
                                                identifierOf("a"),
                                                PLUS_TOKEN,
                                                new InfixExpression(
                                                        identifierOf("b"),
                                                        TIMES_TOKEN,
                                                        identifierOf("c")))))),
                Arguments.of(
                        "a + b * c + d / e - f",
                        List.of(
                                new ExpressionStatement(
                                        identifierTokenOf("f"),
                                        new InfixExpression(
                                                new InfixExpression(
                                                        new InfixExpression(
                                                                identifierOf("a"),
                                                                PLUS_TOKEN,
                                                                new InfixExpression(
                                                                        identifierOf("b"),
                                                                        TIMES_TOKEN,
                                                                        identifierOf("c"))),
                                                        PLUS_TOKEN,
                                                        new InfixExpression(
                                                                identifierOf("d"),
                                                                SLASH_TOKEN,
                                                                identifierOf("e"))),
                                                MINUS_TOKEN,
                                                identifierOf("f"))))),
                //                3 + 4;-5 * 5
                //                (3 + 4)((-5) * 5)
                Arguments.of(
                        "3 + 4;-5 * 5",
                        List.of(
                                new ExpressionStatement(
                                        new Token(TokenType.INT, "4"),
                                        new InfixExpression(
                                                integerLiteralExpressionOf(3),
                                                PLUS_TOKEN,
                                                integerLiteralExpressionOf(4))),
                                new ExpressionStatement(
                                        new Token(TokenType.INT, "5"),
                                        new InfixExpression(
                                                new PrefixExpression(
                                                        MINUS_TOKEN, integerLiteralExpressionOf(5)),
                                                TIMES_TOKEN,
                                                integerLiteralExpressionOf(5))))));
    }

    private static Identifier identifierOf(String literal) {
        return new Identifier(identifierTokenOf(literal));
    }

    private static Token identifierTokenOf(String literal) {
        return new Token(TokenType.IDENT, literal);
    }

    private static IntegerLiteralExpression integerLiteralExpressionOf(Integer val) {
        return new IntegerLiteralExpression(new Token(TokenType.INT, val.toString()), val);
    }
}

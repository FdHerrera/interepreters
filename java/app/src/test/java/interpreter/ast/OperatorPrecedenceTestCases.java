package interpreter.ast;

import static interpreter.ast.ParserTestHelpers.EQ_TOKEN;
import static interpreter.ast.ParserTestHelpers.GT_TOKEN;
import static interpreter.ast.ParserTestHelpers.LT_TOKEN;
import static interpreter.ast.ParserTestHelpers.MINUS_TOKEN;
import static interpreter.ast.ParserTestHelpers.NOT_EQ_TOKEN;
import static interpreter.ast.ParserTestHelpers.PLUS_TOKEN;
import static interpreter.ast.ParserTestHelpers.SLASH_TOKEN;
import static interpreter.ast.ParserTestHelpers.TIMES_TOKEN;
import static interpreter.ast.ParserTestHelpers.identifierOf;
import static interpreter.ast.ParserTestHelpers.identifierTokenOf;
import static interpreter.ast.ParserTestHelpers.integerLiteralExpressionOf;
import static interpreter.ast.ParserTestHelpers.integerTokenOf;

import interpreter.token.Token;
import interpreter.token.TokenType;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.params.provider.Arguments;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OperatorPrecedenceTestCases {

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
                                                integerLiteralExpressionOf(5))))),
                Arguments.of(
                        "5 > 4 == 3 < 4",
                        List.of(
                                new ExpressionStatement(
                                        integerTokenOf(4),
                                        new InfixExpression(
                                                new InfixExpression(
                                                        integerLiteralExpressionOf(5),
                                                        GT_TOKEN,
                                                        integerLiteralExpressionOf(4)),
                                                EQ_TOKEN,
                                                new InfixExpression(
                                                        integerLiteralExpressionOf(3),
                                                        LT_TOKEN,
                                                        integerLiteralExpressionOf(4)))))),
                Arguments.of(
                        "5 < 4 != 3 > 4",
                        List.of(
                                new ExpressionStatement(
                                        integerTokenOf(4),
                                        new InfixExpression(
                                                new InfixExpression(
                                                        integerLiteralExpressionOf(5),
                                                        LT_TOKEN,
                                                        integerLiteralExpressionOf(4)),
                                                NOT_EQ_TOKEN,
                                                new InfixExpression(
                                                        integerLiteralExpressionOf(3),
                                                        GT_TOKEN,
                                                        integerLiteralExpressionOf(4)))))),
                // 3 + 4 * 5 == 3 * 1 + 4 * 5
                // ((3 + (4 * 5)) == ((3 * 1) + (4 * 5))),
                Arguments.of(
                        "3 + 4 * 5 == 3 * 1 + 4 * 5",
                        List.of(
                                new ExpressionStatement(
                                        integerTokenOf(5),
                                        new InfixExpression(
                                                new InfixExpression(
                                                        integerLiteralExpressionOf(3),
                                                        PLUS_TOKEN,
                                                        new InfixExpression(
                                                                integerLiteralExpressionOf(4),
                                                                TIMES_TOKEN,
                                                                integerLiteralExpressionOf(5))),
                                                EQ_TOKEN,
                                                new InfixExpression(
                                                        new InfixExpression(
                                                                integerLiteralExpressionOf(3),
                                                                TIMES_TOKEN,
                                                                integerLiteralExpressionOf(1)),
                                                        PLUS_TOKEN,
                                                        new InfixExpression(
                                                                integerLiteralExpressionOf(4),
                                                                TIMES_TOKEN,
                                                                integerLiteralExpressionOf(5))))))),
                // 3 + 4 * 5 == 3 * 1 + 4 * 5
                // ((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))
                Arguments.of(
                        "3 + 4 * 5 == 3 * 1 + 4 * 5",
                        List.of(
                                new ExpressionStatement(
                                        integerTokenOf(5),
                                        new InfixExpression(
                                                new InfixExpression(
                                                        integerLiteralExpressionOf(3),
                                                        PLUS_TOKEN,
                                                        new InfixExpression(
                                                                integerLiteralExpressionOf(4),
                                                                TIMES_TOKEN,
                                                                integerLiteralExpressionOf(5))),
                                                EQ_TOKEN,
                                                new InfixExpression(
                                                        new InfixExpression(
                                                                integerLiteralExpressionOf(3),
                                                                TIMES_TOKEN,
                                                                integerLiteralExpressionOf(1)),
                                                        PLUS_TOKEN,
                                                        new InfixExpression(
                                                                integerLiteralExpressionOf(4),
                                                                TIMES_TOKEN,
                                                                integerLiteralExpressionOf(
                                                                        5))))))));
    }
}

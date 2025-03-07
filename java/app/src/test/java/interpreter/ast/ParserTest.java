package interpreter.ast;

import static interpreter.ast.ParserTestHelpers.ASTERISK_TOKEN;
import static interpreter.ast.ParserTestHelpers.EQ_TOKEN;
import static interpreter.ast.ParserTestHelpers.FALSE_TOKEN;
import static interpreter.ast.ParserTestHelpers.GT_TOKEN;
import static interpreter.ast.ParserTestHelpers.LT_TOKEN;
import static interpreter.ast.ParserTestHelpers.MINUS_TOKEN;
import static interpreter.ast.ParserTestHelpers.NOT_EQ_TOKEN;
import static interpreter.ast.ParserTestHelpers.PLUS_TOKEN;
import static interpreter.ast.ParserTestHelpers.SLASH_TOKEN;
import static interpreter.ast.ParserTestHelpers.TRUE_TOKEN;
import static interpreter.ast.ParserTestHelpers.booleanLiteralExpressionOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.assertj.core.api.InstanceOfAssertFactories.collection;

import interpreter.lexer.Lexer;
import interpreter.token.Token;
import interpreter.token.TokenType;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParserTest {

    @Test
    void testLetStatements() {
        String input =
                """
                let x = 5;
                let y = 10;
                let foobar = 838383;
                """;
        var lexer = new Lexer(input);
        var parser = Parser.build(lexer);

        var actual = parser.parseProgram();
        var errors = parser.getErrors();

        assertThat(errors).isEmpty();

        assertThat(actual)
                .isNotNull()
                .extracting(Program::statements)
                .asInstanceOf(LIST)
                .hasSize(3)
                .isEqualTo(
                        List.of(
                                new LetStatement(
                                        new Token(TokenType.LET, "let"),
                                        new Identifier(new Token(TokenType.IDENT, "x"))),
                                new LetStatement(
                                        new Token(TokenType.LET, "let"),
                                        new Identifier(new Token(TokenType.IDENT, "y"))),
                                new LetStatement(
                                        new Token(TokenType.LET, "let"),
                                        new Identifier(new Token(TokenType.IDENT, "foobar")))));
    }

    @Test
    void testReturnStatements() {
        String input =
                """
                return 5;
                return 10;
                return 993322;
                """;
        var lexer = new Lexer(input);
        var parser = Parser.build(lexer);

        var actual = parser.parseProgram();
        var errors = parser.getErrors();

        assertThat(errors).isEmpty();

        assertThat(actual)
                .isNotNull()
                .extracting(Program::statements)
                .asInstanceOf(LIST)
                .hasSize(3)
                .isEqualTo(
                        List.of(
                                new ReturnStatement(new Token(TokenType.RETURN, "return"), null),
                                new ReturnStatement(new Token(TokenType.RETURN, "return"), null),
                                new ReturnStatement(new Token(TokenType.RETURN, "return"), null)));
    }

    @Test
    void testIdentifierExpressions() {
        String input = "foobar;";

        var lexer = new Lexer(input);
        var parser = Parser.build(lexer);

        var actual = parser.parseProgram();
        var errors = parser.getErrors();

        assertThat(errors).isEmpty();

        assertThat(actual)
                .isNotNull()
                .extracting(Program::statements)
                .asInstanceOf(LIST)
                .hasSize(1)
                .containsExactly(
                        new ExpressionStatement(
                                new Token(TokenType.IDENT, "foobar"),
                                new Identifier(new Token(TokenType.IDENT, "foobar"))));
    }

    @Test
    void testIntegerLiteralExpressions() {
        String input = "5;";

        var lexer = new Lexer(input);
        var parser = Parser.build(lexer);

        var actual = parser.parseProgram();
        var errors = parser.getErrors();

        assertThat(errors).isEmpty();

        assertThat(actual)
                .isNotNull()
                .extracting(Program::statements)
                .asInstanceOf(LIST)
                .hasSize(1)
                .containsExactly(
                        new ExpressionStatement(
                                new Token(TokenType.INT, "5"), integerLiteralExpressionOf(5)));
    }

    static Stream<Arguments> testBooleanLiteralExpressions_arguments() {
        return Stream.of(
                Arguments.of(
                        "true",
                        List.of(
                                new ExpressionStatement(
                                        TRUE_TOKEN, booleanLiteralExpressionOf(true)))),
                Arguments.of(
                        "false",
                        List.of(
                                new ExpressionStatement(
                                        FALSE_TOKEN, booleanLiteralExpressionOf(false)))),
                Arguments.of(
                        "3 > 5 == false",
                        List.of(
                                new ExpressionStatement(
                                        FALSE_TOKEN,
                                        new InfixExpression(
                                                new InfixExpression(
                                                        ParserTestHelpers
                                                                .integerLiteralExpressionOf(3),
                                                        GT_TOKEN,
                                                        integerLiteralExpressionOf(5)),
                                                EQ_TOKEN,
                                                booleanLiteralExpressionOf(false))))),
                Arguments.of(
                        "3 < 5 == true",
                        List.of(
                                new ExpressionStatement(
                                        TRUE_TOKEN,
                                        new InfixExpression(
                                                new InfixExpression(
                                                        ParserTestHelpers
                                                                .integerLiteralExpressionOf(3),
                                                        LT_TOKEN,
                                                        integerLiteralExpressionOf(5)),
                                                EQ_TOKEN,
                                                booleanLiteralExpressionOf(true))))));
    }

    @ParameterizedTest
    @MethodSource("interpreter.ast.ParserTest#testBooleanLiteralExpressions_arguments")
    void testBooleanLiteralExpressions(String input, List<Statement> expectedStatements) {
        var lexer = new Lexer(input);
        var parser = Parser.build(lexer);

        var actual = parser.parseProgram();
        var errors = parser.getErrors();

        assertThat(errors).isEmpty();

        assertThat(actual)
                .isNotNull()
                .extracting(Program::statements)
                .asInstanceOf(LIST)
                .hasSize(expectedStatements.size())
                .isEqualTo(expectedStatements);
    }

    private static Stream<Arguments> prefixExpressionsTests() {
        return Stream.of(
                Arguments.of("!5;", new Token(TokenType.BANG, "!"), 5),
                Arguments.of("-15;", new Token(TokenType.MINUS, "-"), 15));
    }

    @ParameterizedTest
    @MethodSource("interpreter.ast.ParserTest#prefixExpressionsTests")
    void testPrefixExpressions(String input, Token expectedToken, Integer expectedIntegerValue) {
        var lexer = new Lexer(input);
        var parser = Parser.build(lexer);

        var actual = parser.parseProgram();
        var errors = parser.getErrors();

        assertThat(errors).isEmpty();

        var expectedLiteral = integerLiteralExpressionOf(expectedIntegerValue);

        assertThat(actual)
                .isNotNull()
                .extracting(Program::statements)
                .asInstanceOf(LIST)
                .hasSize(1)
                .containsExactly(
                        new ExpressionStatement(
                                new Token(TokenType.INT, expectedIntegerValue.toString()),
                                new PrefixExpression(expectedToken, expectedLiteral)));
    }

    private static Stream<Arguments> infixExpressionsTests() {
        return Stream.of(
                Arguments.of(
                        "5 + 5;",
                        integerLiteralExpressionOf(5),
                        PLUS_TOKEN,
                        integerLiteralExpressionOf(5)),
                Arguments.of(
                        "5 - 5;",
                        integerLiteralExpressionOf(5),
                        MINUS_TOKEN,
                        integerLiteralExpressionOf(5)),
                Arguments.of(
                        "5 * 5;",
                        integerLiteralExpressionOf(5),
                        ASTERISK_TOKEN,
                        integerLiteralExpressionOf(5)),
                Arguments.of(
                        "5 / 5;",
                        integerLiteralExpressionOf(5),
                        SLASH_TOKEN,
                        integerLiteralExpressionOf(5)),
                Arguments.of(
                        "5 > 5;",
                        integerLiteralExpressionOf(5),
                        GT_TOKEN,
                        integerLiteralExpressionOf(5)),
                Arguments.of(
                        "5 < 5;",
                        integerLiteralExpressionOf(5),
                        LT_TOKEN,
                        integerLiteralExpressionOf(5)),
                Arguments.of(
                        "5 == 5;",
                        integerLiteralExpressionOf(5),
                        EQ_TOKEN,
                        integerLiteralExpressionOf(5)),
                Arguments.of(
                        "5 != 5;",
                        integerLiteralExpressionOf(5),
                        NOT_EQ_TOKEN,
                        integerLiteralExpressionOf(5)),
                Arguments.of(
                        "true == true",
                        booleanLiteralExpressionOf(true),
                        EQ_TOKEN,
                        booleanLiteralExpressionOf(true)),
                Arguments.of(
                        "true != true",
                        booleanLiteralExpressionOf(true),
                        NOT_EQ_TOKEN,
                        booleanLiteralExpressionOf(true)),
                Arguments.of(
                        "false == false",
                        booleanLiteralExpressionOf(false),
                        EQ_TOKEN,
                        booleanLiteralExpressionOf(false)));
    }

    @ParameterizedTest
    @MethodSource("interpreter.ast.ParserTest#infixExpressionsTests")
    void testInfixExpressions(
            String input,
            Expression expectedLeftExpression,
            Token expectedOperator,
            Expression expectedRightExpression) {
        var lexer = new Lexer(input);
        var parser = Parser.build(lexer);

        var actual = parser.parseProgram();
        var errors = parser.getErrors();

        assertThat(errors).isEmpty();

        assertThat(actual)
                .isNotNull()
                .extracting(Program::statements)
                .asInstanceOf(InstanceOfAssertFactories.collection(ExpressionStatement.class))
                .hasSize(1)
                .extracting(ExpressionStatement::expression)
                .containsExactly(
                        new InfixExpression(
                                expectedLeftExpression, expectedOperator, expectedRightExpression));
    }

    @ParameterizedTest
    @MethodSource("interpreter.ast.OperatorPrecedenceTestCases#testCases")
    void testOperatorPrecedence(String input, List<ExpressionStatement> expectedStatements) {
        var lexer = new Lexer(input);
        var parser = Parser.build(lexer);

        var actual = parser.parseProgram();
        var errors = parser.getErrors();

        assertThat(errors).isEmpty();

        assertThat(actual)
                .isNotNull()
                .extracting(Program::statements)
                .asInstanceOf(collection(Statement.class))
                .isEqualTo(expectedStatements);
    }

    private static IntegerLiteralExpression integerLiteralExpressionOf(Integer val) {
        return new IntegerLiteralExpression(new Token(TokenType.INT, val.toString()), val);
    }
}

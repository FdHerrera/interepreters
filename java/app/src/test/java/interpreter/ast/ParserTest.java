package interpreter.ast;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

import interpreter.lexer.Lexer;
import interpreter.token.Token;
import interpreter.token.TokenType;
import java.util.List;
import java.util.stream.Stream;
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
                                new Token(TokenType.INT, "5"),
                                new IntegerLiteralExpression(new Token(TokenType.INT, "5"), 5)));
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

        var expectedLiteral =
                new IntegerLiteralExpression(
                        new Token(TokenType.INT, expectedIntegerValue.toString()),
                        expectedIntegerValue);

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
}

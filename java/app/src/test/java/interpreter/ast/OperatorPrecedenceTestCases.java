package interpreter.ast;

import interpreter.token.Token;
import interpreter.token.TokenType;
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
                        new InfixExpression(
                                new PrefixExpression(MINUS_TOKEN, identifierOf("a")),
                                TIMES_TOKEN,
                                identifierOf("b"))),
                Arguments.of(
                        "!-a",
                        new PrefixExpression(
                                new Token(TokenType.BANG, "!"),
                                new PrefixExpression(MINUS_TOKEN, identifierOf("a")))),
                Arguments.of(
                        "a + b + c",
                        new InfixExpression(
                                new InfixExpression(
                                        identifierOf("a"), PLUS_TOKEN, identifierOf("b")),
                                PLUS_TOKEN,
                                identifierOf("c"))),
                Arguments.of(
                        "a + b - c",
                        new InfixExpression(
                                new InfixExpression(
                                        identifierOf("a"), PLUS_TOKEN, identifierOf("b")),
                                MINUS_TOKEN,
                                identifierOf("c"))),
                Arguments.of(
                        "a * b * c",
                        new InfixExpression(
                                new InfixExpression(
                                        identifierOf("a"), TIMES_TOKEN, identifierOf("b")),
                                TIMES_TOKEN,
                                identifierOf("c"))),
                Arguments.of(
                        "a * b / c",
                        new InfixExpression(
                                new InfixExpression(
                                        identifierOf("a"), TIMES_TOKEN, identifierOf("b")),
                                SLASH_TOKEN,
                                identifierOf("c"))),
                Arguments.of(
                        "a + b / c",
                        new InfixExpression(
                                identifierOf("a"),
                                PLUS_TOKEN,
                                new InfixExpression(
                                        identifierOf("b"), SLASH_TOKEN, identifierOf("c")))),
                Arguments.of(
                        "a + b * c",
                        new InfixExpression(
                                identifierOf("a"),
                                PLUS_TOKEN,
                                new InfixExpression(
                                        identifierOf("b"), TIMES_TOKEN, identifierOf("c")))),
                Arguments.of(
                        "a + b * c + d / e - f",
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
                                                identifierOf("d"), SLASH_TOKEN, identifierOf("e"))),
                                MINUS_TOKEN,
                                identifierOf("f"))));
    }

    private static Identifier identifierOf(String literal) {
        return new Identifier(new Token(TokenType.IDENT, literal));
    }
}

package interpreter.token;

public record Token(
        TokenType type,
        String literal) {
}

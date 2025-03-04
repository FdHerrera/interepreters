package interpreter.token;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtil {
    private static final Map<String, TokenType> KEYWORDS = Map.of(
            "else", TokenType.ELSE,
            "false", TokenType.FALSE,
            "fn", TokenType.FUNCTION,
            "if", TokenType.IF,
            "let", TokenType.LET,
            "return", TokenType.RETURN,
            "true", TokenType.TRUE
    );

    public static TokenType getTypeForIdentifier(String identifier) {
        return KEYWORDS.getOrDefault(identifier, TokenType.IDENT);
    }
}
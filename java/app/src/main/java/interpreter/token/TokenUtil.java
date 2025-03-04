package interpreter.token;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtil {
    private static final Map<String, TokenType> KEYWORDS = Map.of(
            "fn", TokenType.FUNCTION,
            "let", TokenType.LET
    );

    public static TokenType getTypeForIdentifier(String identifier) {
        return KEYWORDS.getOrDefault(identifier, TokenType.IDENT);
    }
}
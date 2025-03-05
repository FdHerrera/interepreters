package interpreter.utils;

import interpreter.ast.Precedence;
import interpreter.token.TokenType;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrecedenceUtils {
    private static final Map<TokenType, Precedence> TOKEN_TYPE_TO_PRECEDENCE =
            Map.of(
                    TokenType.EQ, Precedence.EQUALS,
                    TokenType.NOT_EQ, Precedence.EQUALS,
                    TokenType.LT, Precedence.LESSGREATER,
                    TokenType.GT, Precedence.LESSGREATER,
                    TokenType.PLUS, Precedence.SUM,
                    TokenType.MINUS, Precedence.SUM,
                    TokenType.SLASH, Precedence.PRODUCT,
                    TokenType.ASTERISK, Precedence.PRODUCT);

    public static Precedence precedenceFor(TokenType tokenType) {
        return TOKEN_TYPE_TO_PRECEDENCE.getOrDefault(tokenType, Precedence.LOWEST);
    }
}

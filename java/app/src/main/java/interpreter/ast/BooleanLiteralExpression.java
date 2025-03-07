package interpreter.ast;

import interpreter.token.Token;
import interpreter.token.TokenType;
import org.apache.commons.lang3.Validate;

public record BooleanLiteralExpression(Token token, Boolean value) implements Expression {
    public BooleanLiteralExpression {
        Validate.notNull(token, "token must not be null");
        Validate.notNull(value, "value must not be null");
        Validate.isTrue(
                token.type().equals(TokenType.TRUE) || token.type().equals(TokenType.FALSE),
                "token type should be TokenType#TRUE or TokenType#FALSE");
    }
}

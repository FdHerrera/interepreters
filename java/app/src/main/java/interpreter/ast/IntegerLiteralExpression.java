package interpreter.ast;

import interpreter.token.Token;
import interpreter.token.TokenType;
import org.apache.commons.lang3.Validate;

public record IntegerLiteralExpression(Token token, Integer value) implements Expression {
    public IntegerLiteralExpression {
        Validate.notNull(token, "token must not be null");
        Validate.notNull(value, "value must not be null");
        Validate.isTrue(TokenType.INT.equals(token.type()), "token type should be TokenType#INT");
    }
}

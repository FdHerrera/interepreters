package interpreter.ast;

import interpreter.token.Token;
import interpreter.token.TokenType;
import org.apache.commons.lang3.Validate;

public record PrefixExpression(Token token, Expression right) implements Expression {
    public PrefixExpression {
        Validate.notNull(token, "token must not be null");
        Validate.notNull(right, "right must not be null");
        Validate.isTrue(
                TokenType.BANG.equals(token.type()) || TokenType.MINUS.equals(token.type()),
                "token should be of type %s or %s",
                TokenType.BANG,
                TokenType.MINUS);
    }
}

package interpreter.ast;

import interpreter.token.Token;
import org.apache.commons.lang3.Validate;

public record InfixExpression(Expression left, Token operator, Expression right)
        implements Expression {
    public InfixExpression {
        Validate.notNull(left, "left expression should not be null");
        Validate.notNull(operator, "operand should not be null");
        Validate.notNull(right, "righ expression should not be null");
    }
}

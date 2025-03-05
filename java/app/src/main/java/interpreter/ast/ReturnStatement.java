package interpreter.ast;

import interpreter.token.Token;
import interpreter.token.TokenType;
import org.apache.commons.lang3.Validate;

public record ReturnStatement(Token token, Expression expression) implements Statement {
    public ReturnStatement {
        Validate.notNull(token, "token should not be null");
        //        Validate.notNull(expression, "expression should not be null"); for now
        Validate.isTrue(TokenType.RETURN.equals(token.type()), "should be TokenType#Return");
    }
}

package interpreter.ast;

import interpreter.token.Token;
import interpreter.token.TokenType;
import org.apache.commons.lang3.Validate;

public record LetStatement(Token token, Identifier identifier) implements Statement {
    public LetStatement {
        Validate.notNull(token, "token should not be null");
        Validate.notNull(identifier, "identifier should not be null");
        Validate.isTrue(TokenType.LET.equals(token.type()), "should be TokenType#LET");
    }
}

package interpreter.ast;

import interpreter.token.Token;
import interpreter.token.TokenType;
import org.apache.commons.lang3.Validate;

public record Identifier(Token token) implements Expression {
    public Identifier {
        Validate.notNull(token, "token should not be null");
        Validate.isTrue(
                TokenType.IDENT.equals(token.type()), "token type should be TokenType#IDENT");
    }
}

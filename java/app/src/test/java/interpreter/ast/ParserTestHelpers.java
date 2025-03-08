package interpreter.ast;

import interpreter.token.Token;
import interpreter.token.TokenType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class ParserTestHelpers {
    public static final Token MINUS_TOKEN = new Token(TokenType.MINUS, "-");
    public static final Token PLUS_TOKEN = new Token(TokenType.PLUS, "+");
    public static final Token TIMES_TOKEN = new Token(TokenType.ASTERISK, "*");
    public static final Token SLASH_TOKEN = new Token(TokenType.SLASH, "/");
    public static final Token GT_TOKEN = new Token(TokenType.GT, ">");
    public static final Token LT_TOKEN = new Token(TokenType.LT, "<");
    public static final Token EQ_TOKEN = new Token(TokenType.EQ, "==");
    public static final Token NOT_EQ_TOKEN = new Token(TokenType.NOT_EQ, "!=");
    public static final Token TRUE_TOKEN = new Token(TokenType.TRUE, "true");
    public static final Token FALSE_TOKEN = new Token(TokenType.FALSE, "false");
    public static final Token ASTERISK_TOKEN = new Token(TokenType.ASTERISK, "*");
    public static final Token RPAREN_TOKEN = new Token(TokenType.RPAREN, ")");
    public static final Token BANG_TOKEN = new Token(TokenType.BANG, "!");

    public static Identifier identifierOf(String literal) {
        return new Identifier(identifierTokenOf(literal));
    }

    public static Token identifierTokenOf(String literal) {
        return new Token(TokenType.IDENT, literal);
    }

    public static IntegerLiteralExpression integerLiteralExpressionOf(Integer val) {
        return new IntegerLiteralExpression(integerTokenOf(val), val);
    }

    public static Token integerTokenOf(Integer val) {
        return new Token(TokenType.INT, val.toString());
    }

    public static BooleanLiteralExpression booleanLiteralExpressionOf(Boolean literal) {
        return new BooleanLiteralExpression(
                new Token(literal ? TokenType.TRUE : TokenType.FALSE, literal.toString()), literal);
    }
}

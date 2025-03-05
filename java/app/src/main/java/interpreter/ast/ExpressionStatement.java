package interpreter.ast;

import interpreter.token.Token;

public record ExpressionStatement(Token token, Expression expression) implements Statement {}

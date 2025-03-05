package interpreter.ast;

import static interpreter.utils.PrecedenceUtils.precedenceFor;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import interpreter.lexer.Lexer;
import interpreter.token.Token;
import interpreter.token.TokenType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Parser {
    private final Lexer lexer;
    @Getter private final List<String> errors;
    private Token currToken;
    private Token peekToken;

    private final Map<TokenType, Supplier<Expression>> prefixParseFns =
            Map.of(
                    TokenType.IDENT,
                    () -> new Identifier(currToken),
                    TokenType.INT,
                    () ->
                            new IntegerLiteralExpression(
                                    currToken, Integer.valueOf(currToken.literal())),
                    TokenType.BANG,
                    this::parsePrefixExpression,
                    TokenType.MINUS,
                    this::parsePrefixExpression);

    private final Map<TokenType, UnaryOperator<Expression>> infixParseFns =
            Map.of(
                    TokenType.EQ, this::parseInfixExpression,
                    TokenType.NOT_EQ, this::parseInfixExpression,
                    TokenType.LT, this::parseInfixExpression,
                    TokenType.GT, this::parseInfixExpression,
                    TokenType.PLUS, this::parseInfixExpression,
                    TokenType.MINUS, this::parseInfixExpression,
                    TokenType.SLASH, this::parseInfixExpression,
                    TokenType.ASTERISK, this::parseInfixExpression);

    public static Parser build(Lexer lexer) {
        Validate.notNull(lexer, "lexer should not be null");
        Parser parser = new Parser(lexer, new ArrayList<>());

        // Make sure currToken and peekToken are set
        parser.nextToken();
        parser.nextToken();

        return parser;
    }

    private void nextToken() {
        currToken = peekToken;
        peekToken = lexer.nextToken();
    }

    public Program parseProgram() {
        List<Statement> statements = new ArrayList<>();
        while (!TokenType.EOF.equals(currToken.type())) {
            var statement = parseStatement();
            if (nonNull(statement)) {
                statements.add(statement);
            }
            nextToken();
        }
        return new Program(statements);
    }

    private Statement parseStatement() {
        return switch (currToken.type()) {
            case LET -> parseLetStatement();
            case RETURN -> parseReturnStatement();
            default -> parseExpressionStatement();
        };
    }

    private LetStatement parseLetStatement() {
        if (!expectPeek(TokenType.IDENT)) {
            return null;
        }

        LetStatement statement = new LetStatement(currToken, new Identifier(peekToken));
        nextToken();

        if (!expectPeek(TokenType.ASSIGN)) {
            return null;
        }
        while (!currToken.type().equals(TokenType.SEMICOLON)) {
            nextToken();
        }
        return statement;
    }

    private ReturnStatement parseReturnStatement() {
        ReturnStatement statement = new ReturnStatement(currToken, null /*for now*/);

        do {
            nextToken();
        } while (!currToken.type().equals(TokenType.SEMICOLON));
        return statement;
    }

    private Statement parseExpressionStatement() {
        var expression = parseExpression(Precedence.LOWEST);
        var statement = new ExpressionStatement(currToken, expression);

        while (peekToken.type().equals(TokenType.SEMICOLON)) {
            nextToken();
        }
        return statement;
    }

    private Expression parseExpression(Precedence precedence) {
        var prefix = prefixParseFns.get(currToken.type());
        if (isNull(prefix)) {
            noPrefixParseFnError(currToken.type());
            return null;
        }
        var leftExpression = prefix.get();

        while (!peekToken.type().equals(TokenType.SEMICOLON)
                && precedence.ordinal() < precedenceFor(peekToken.type()).ordinal()) {
            UnaryOperator<Expression> infixParseFunction = infixParseFns.get(peekToken.type());
            if (isNull(infixParseFunction)) {
                return leftExpression;
            }
            nextToken();
            leftExpression = infixParseFunction.apply(leftExpression);
        }

        return leftExpression;
    }

    private Expression parsePrefixExpression() {
        Token prefixOperator = currToken;

        nextToken();

        Expression rightExpression = parseExpression(Precedence.PREFIX);

        return new PrefixExpression(prefixOperator, rightExpression);
    }

    private Expression parseInfixExpression(Expression left) {
        Token operator = currToken;
        Precedence precedence = precedenceFor(operator.type());
        nextToken();
        Expression right = parseExpression(precedence);
        return new InfixExpression(left, operator, right);
    }

    private boolean expectPeek(TokenType expectedPeekType) {
        if (expectedPeekType.equals(peekToken.type())) {
            return true;
        } else {
            peekError(expectedPeekType);
            return false;
        }
    }

    private void peekError(TokenType expectedTokenType) {
        errors.add(
                "expecting next token to be %s, got %s instead"
                        .formatted(expectedTokenType, peekToken.type()));
    }

    private void noPrefixParseFnError(TokenType prefixToken) {
        errors.add("no prefix parse function found for type: %s".formatted(prefixToken));
    }
}

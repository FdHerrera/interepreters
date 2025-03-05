package interpreter.ast;

import interpreter.lexer.Lexer;
import interpreter.token.Token;
import interpreter.token.TokenType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Parser {
    private final Lexer lexer;
    @Getter
    private final List<String> errors;
    private Token currToken;
    private Token peekToken;

    private final Map<TokenType, Supplier<Expression>> prefixParseFns = Map.of(
            TokenType.IDENT, () -> new Identifier(currToken)
    );

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
            return null;
        }
        return prefix.get();
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
                        .formatted(expectedTokenType, peekToken.type())
        );
    }
}

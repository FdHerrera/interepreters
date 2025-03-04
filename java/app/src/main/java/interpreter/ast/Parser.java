package interpreter.ast;

import interpreter.lexer.Lexer;
import interpreter.token.Token;
import interpreter.token.TokenType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Parser {
    private final Lexer lexer;
    private Token currToken;
    private Token peekToken;

    public static Parser build(Lexer lexer) {
        Validate.notNull(lexer, "lexer should not be null");
        Parser parser = new Parser(lexer);

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
            default -> null;
        };
    }

    private LetStatement parseLetStatement() {
        if (!TokenType.IDENT.equals(peekToken.type())) {
            return null;
        }

        return new LetStatement(currToken, new Identifier(peekToken));
    }
}

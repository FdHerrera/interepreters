package interpreter.ast;

import interpreter.lexer.Lexer;
import interpreter.token.Token;
import interpreter.token.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

class ParserTest {

    @Test
    void testLetStatements() {
        String input = """
                let x = 5;
                let y = 10;
                let foobar = 838383;
                """;
        var lexer = new Lexer(input);
        var parser = Parser.build(lexer);

        var actual = parser.parseProgram();
        var errors = parser.getErrors();

        assertThat(errors).isEmpty();

        assertThat(actual).isNotNull()
                .extracting(Program::statements).asInstanceOf(LIST)
                .hasSize(3)
                .isEqualTo(
                        List.of(
                                new LetStatement(new Token(TokenType.LET, "let"), new Identifier(new Token(TokenType.IDENT, "x"))),
                                new LetStatement(new Token(TokenType.LET, "let"), new Identifier(new Token(TokenType.IDENT, "y"))),
                                new LetStatement(new Token(TokenType.LET, "let"), new Identifier(new Token(TokenType.IDENT, "foobar")))
                        )
                );
    }

}
package interpreter.repl;

import interpreter.lexer.Lexer;
import interpreter.token.Token;
import interpreter.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Repl {
    private static final String PROMPT = ">> ";

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(PROMPT);
        while (scanner.hasNext()) {
            String userInput = scanner.next();
            Lexer lexer = new Lexer(userInput);
            List<Token> tokens = new ArrayList<>();
            Token token = lexer.nextToken();
            while (!token.type().equals(TokenType.EOF)) {
                tokens.add(token);
                token = lexer.nextToken();
            }
            System.out.println(tokens);
        }
    }
}

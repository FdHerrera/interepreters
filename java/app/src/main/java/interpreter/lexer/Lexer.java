package interpreter.lexer;

import interpreter.token.Token;
import interpreter.token.TokenType;
import lombok.Getter;

@Getter
public class Lexer {
    private final String input;
    private int position = 0;
    private int readPosition = 0;
    private char ch = '\0';

    public Lexer(String input) {
        this.input = input;
        readChar();
    }

    public void readChar() {
        if (readPosition >= input.length()) {
            ch = '\0';
        } else {
            ch = input.charAt(readPosition);
        }
        position = readPosition;
        readPosition++;
    }

    public Token nextToken() {
        Token token = switch (ch) {
            case '=' -> newToken(TokenType.ASSIGN, ch);
            case '+' -> newToken(TokenType.PLUS, ch);
            case '(' -> newToken(TokenType.LPAREN, ch);
            case ')' -> newToken(TokenType.RPAREN, ch);
            case '{' -> newToken(TokenType.LBRACE, ch);
            case '}' -> newToken(TokenType.RBRACE, ch);
            case ',' -> newToken(TokenType.COMMA, ch);
            case ';' -> newToken(TokenType.SEMICOLON, ch);
            case '\0' -> newToken(TokenType.EOF, ch);
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
        readChar();
        return token;
    }

    private Token newToken(TokenType type, char ch) {
        return new Token(type, String.valueOf(ch));
    }
}

package interpreter.lexer;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.lang.Character.isWhitespace;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;

import interpreter.token.Token;
import interpreter.token.TokenType;
import interpreter.utils.TokenUtil;
import java.util.function.Predicate;
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
        while (isWhitespace(ch)) {
            readChar();
        }
        return switch (ch) {
            case '=' -> {
                if (peekChar() == '=') {
                    readChar();
                    readChar();
                    yield new Token(TokenType.EQ, "==");
                } else {
                    yield newToken(TokenType.ASSIGN, ch);
                }
            }
            case '*' -> newToken(TokenType.ASTERISK, ch);
            case '!' -> {
                if (peekChar() == '=') {
                    readChar();
                    readChar();
                    yield new Token(TokenType.NOT_EQ, "!=");
                } else {
                    yield newToken(TokenType.BANG, ch);
                }
            }
            case '>' -> newToken(TokenType.GT, ch);
            case '<' -> newToken(TokenType.LT, ch);
            case '-' -> newToken(TokenType.MINUS, ch);
            case '+' -> newToken(TokenType.PLUS, ch);
            case '/' -> newToken(TokenType.SLASH, ch);
            case '(' -> newToken(TokenType.LPAREN, ch);
            case ')' -> newToken(TokenType.RPAREN, ch);
            case '{' -> newToken(TokenType.LBRACE, ch);
            case '}' -> newToken(TokenType.RBRACE, ch);
            case ',' -> newToken(TokenType.COMMA, ch);
            case ';' -> newToken(TokenType.SEMICOLON, ch);
            case '\0' -> newToken(TokenType.EOF, ch);
            default -> {
                if (isLetter(ch)) {
                    String identifier = readIdentifier(Character::isLetter);
                    yield new Token(TokenUtil.getTypeForIdentifier(identifier), identifier);
                } else if (isDigit(ch)) {
                    String identifier = readIdentifier(Character::isDigit);
                    if (isDigits(identifier)) {
                        yield new Token(TokenType.INT, identifier);
                    }
                    yield new Token(TokenType.ILLEGAL, identifier);
                } else {
                    yield new Token(TokenType.ILLEGAL, String.valueOf(ch));
                }
            }
        };
    }

    private char peekChar() {
        if (readPosition >= input.length()) {
            return '\0';
        } else {
            return input.charAt(readPosition);
        }
    }

    private String readIdentifier(Predicate<Character> condition) {
        int currentPosition = position;
        while (condition.test(ch) || ch == '_') {
            readChar();
        }
        return input.substring(currentPosition, position);
    }

    private Token newToken(TokenType type, char character) {
        Token token = new Token(type, String.valueOf(character));
        readChar();
        return token;
    }
}

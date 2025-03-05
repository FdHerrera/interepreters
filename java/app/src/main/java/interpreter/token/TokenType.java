package interpreter.token;

public enum TokenType {
    EOF,
    ILLEGAL,

    // Identifier + literals
    IDENT,
    INT,

    // Operators
    ASSIGN,
    ASTERISK,
    BANG,
    GT,
    LT,
    MINUS,
    PLUS,
    SLASH,

    // Delimiters
    COMMA,
    LBRACE,
    LPAREN,
    RBRACE,
    RPAREN,
    SEMICOLON,

    // Keywords
    ELSE,
    EQ,
    FALSE,
    FUNCTION,
    IF,
    LET,
    NOT_EQ,
    RETURN,
    TRUE,
}

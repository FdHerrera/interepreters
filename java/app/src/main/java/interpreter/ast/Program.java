package interpreter.ast;

import java.util.List;

public record Program(List<Statement> statements) implements Statement {}

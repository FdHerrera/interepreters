package interpreter;

import interpreter.repl.Repl;

public class App {

    public static void main(String[] args) {
        System.out.println("Hey there! This is my programming language! Type some commands next!");
        new Repl().start();
    }
}

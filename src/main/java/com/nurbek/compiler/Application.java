package com.nurbek;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Application {
    
    public static void main(String[] args) throws Exception {
        File file = new File("src/main/resources/" + args[0]);

        if (!file.exists()) {
            throw new FileNotFoundException("The file does not exist.");
        }

        StringBuilder source = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                source.append(scanner.nextLine()).append("\n");
            }
        }

        Lexer lexer = new LexerImpl(source.toString());

        try {
            Parser parser = new ParserImpl(lexer);
            parser.program();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
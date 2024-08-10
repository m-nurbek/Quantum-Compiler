package com.nurbek;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        File file = new File("src/main/resources/" + args[0]);

        StringBuilder source = new StringBuilder();

        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()) {
                source.append(scanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            return;
        }

        if (file.exists()) {
            Lexer lexer = new LexerImpl(source.toString());

            try {
                Parser parser = new ParserImpl(lexer);

                parser.program();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println("The file does not exist.");
        }

    }
}
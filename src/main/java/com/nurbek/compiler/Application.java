package com.nurbek.compiler;

import com.nurbek.compiler.component.Emitter;
import com.nurbek.compiler.component.Lexer;
import com.nurbek.compiler.component.impl.EmitterImpl;
import com.nurbek.compiler.component.impl.LexerImpl;
import com.nurbek.compiler.component.Parser;
import com.nurbek.compiler.component.impl.PythonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Application {
    private final Lexer lexer;
    private final Emitter emitter;
    private final Parser parser;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public Application(Lexer lexer, Emitter emitter, Parser parser) {
        this.lexer = lexer;
        this.emitter = emitter;
        this.parser = parser;
    }

    public void compile(String sourceFilePath, String outputFilePath) throws Exception {
        File file = new File(sourceFilePath);

        if (!file.exists()) {
            throw new FileNotFoundException("The file does not exist.");
        }

        StringBuilder source = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                source.append(scanner.nextLine()).append("\n");
            }
        }

        lexer.setSource(source.toString());
        emitter.setOutputFile(outputFilePath);
        parser.setLexer(lexer);
        parser.setEmitter(emitter);

        try {
            parser.program();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        emitter.writeFile();
        log.info("======== Compilation is completed! ========");
    }

    public static void main(String[] args) throws Exception {
        Lexer lexer = new LexerImpl();
        Emitter emitter = new EmitterImpl();
        Parser parser = new PythonParser();

        Application app = new Application(lexer, emitter, parser);
        app.compile("src/main/resources/" + args[0], "src/main/resources/out.py");
    }
}
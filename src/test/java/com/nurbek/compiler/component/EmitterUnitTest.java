package com.nurbek.compiler.component;

import com.nurbek.compiler.component.impl.EmitterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmitterUnitTest {
    private Emitter emitter;
    private Path tempFile;

    @BeforeEach
    void setup() throws IOException {
        tempFile = Files.createTempFile("emitterTest", ".txt");
        emitter = new EmitterImpl();
        emitter.setOutputFile(tempFile.toString());
    }

    @Test
    void emit() throws IOException {
        // given
        String code = "some code";

        // when
        emitter.emit(code);
        emitter.writeFile();

        // then
        String content = Files.readString(tempFile);
        assertEquals("some code", content);
    }

    @Test
    void emitLine() throws IOException {
        // given
        String code = "some code";

        // when
        emitter.emit(code);
        emitter.writeFile();

        // then
        String content = Files.readString(tempFile);
        assertEquals("some code", content);
    }

    @Test
    void headerLine() throws IOException {
        // given
        String code = "header code";

        // when
        emitter.headerLine(code);
        emitter.writeFile();

        // then
        String content = Files.readString(tempFile);
        assertEquals("header code\n", content);
    }

    @Test
    void writeFile() throws IOException {
        // when
        emitter.emit("body code");
        emitter.writeFile();

        // then
        String content = Files.readString(tempFile);
        assertEquals("body code", content);
    }

    @Test
    void setOutputFile() throws IOException {
        // given
        String newOutputFilePath = tempFile.toString();

        // when
        emitter.setOutputFile(newOutputFilePath);
        emitter.emit("new file content");
        emitter.writeFile();

        // then
        File file = new File(newOutputFilePath);
        assertTrue(file.exists());
        String content = Files.readString(file.toPath());
        assertEquals("new file content", content);
    }
}
package com.nurbek.compiler.component.impl;

import com.nurbek.compiler.component.Emitter;
import com.nurbek.compiler.exception.EmitterException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EmitterImpl implements Emitter {
    private String fullPath;
    private final StringBuilder header;
    private final StringBuilder body;
    private final static String DEFAULT_PATH = "./";

    public EmitterImpl() {
        this.fullPath = DEFAULT_PATH;
        header = new StringBuilder();
        body = new StringBuilder();
    }

    @Override
    public void emit(String code) {
        body.append(code);
    }

    @Override
    public void emitLine(String code) {
        body.append(code).append('\n');
    }

    @Override
    public void headerLine(String code) {
        header.append(code).append('\n');
    }

    @Override
    public void writeFile() {
        try {
            File file = new File(fullPath);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new EmitterException(e.getMessage());
        }

        try (FileWriter writer = new FileWriter(fullPath)) {
            writer.write(header.append(body).toString());
        } catch (IOException e) {
            throw new EmitterException(e.getMessage());
        }
    }

    @Override
    public void setOutputFile(String outputFilePath) {
        this.fullPath = outputFilePath;
    }
}
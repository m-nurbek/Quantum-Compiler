package com.nurbek;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EmitterImpl implements Emitter {
    private final String fullPath;
    private StringBuilder header;
    private StringBuilder body;

    public EmitterImpl(String fullPath) {
        this.fullPath = fullPath;
        header = new StringBuilder();
        body =  new StringBuilder();
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
            throw new RuntimeException(e);
        }

        try (FileWriter writer = new FileWriter(fullPath)) {
            writer.write(header.append(body).toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
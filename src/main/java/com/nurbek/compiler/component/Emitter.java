package com.nurbek.compiler.component;

public interface Emitter {

    void emit(String code);

    void emitLine(String code);

    void headerLine(String code);

    void writeFile();

    void setOutputFile(String outputFilePath);
}
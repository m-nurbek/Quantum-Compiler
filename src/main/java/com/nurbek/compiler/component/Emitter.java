package com.nurbek.compiler;

public interface Emitter {

    void emit(String code);

    void emitLine(String code);

    void headerLine(String code);

    void writeFile();

}
package io.github.ex.util;

import io.github.ex.Main;
import io.github.ex.compile.Token;

import java.io.PrintStream;

public class CompileException extends RuntimeException{
    Token token;
    String filename;
    String message;
    int status = 0;

    public CompileException(String message,String filename){
        this.message = message;
        this.filename = filename;
        this.status = 2;
    }
    public CompileException(String message, Token token, String filename){
        this.token = token;
        this.filename = filename;
        this.status = 1;
        this.message = message;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        if (status == 1) {
            s.println("Compile Error: " + message +
                    "\n\tToken: " + token.getData() +
                    "\n\tLine: " + token.getLine() +
                    "\n\tFilename: " + filename +
                    "\n\tCompile: " + Main.compile_version +
                    "\n\tRuntime: " + Main.runtime_version);
        } else if (status == 2) {
            s.println("Compile Error: " + message +
                    "\n\tCompile: " + Main.compile_version +
                    "\n\tRuntime: " + Main.runtime_version);
        }

        s.println("Cause by Thread/"+Thread.currentThread().getName()+" CompileException");
        StackTraceElement[] trace = getStackTrace();
        for (StackTraceElement traceElement : trace)
            s.println("\tat " + traceElement);

    }
}

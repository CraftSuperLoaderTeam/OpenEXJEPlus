package io.github.ex.util;

import io.github.ex.exe.obj.ExObject;

public class ReturnException extends RuntimeException{
    ExObject obj;
    public ReturnException(ExObject obj){
        this.obj = obj;
    }

    public ExObject getObj() {
        return obj;
    }
}

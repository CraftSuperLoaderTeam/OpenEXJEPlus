package io.github.ex.util;

import io.github.ex.Main;
import io.github.ex.exe.thread.ThreadTask;

public class VMRuntimeException extends Exception{
    public VMRuntimeException(String message, ThreadTask task){
        Main.getOutput().error("ScriptRuntimeError:"+message+"\n\t" +
                "ThreadName:"+task.getName()+"\n\t" +
                "FileName:"+task.getFilename()+"\n\t" +
                "RuntimeVersion:"+Main.runtime_version);
    }
}

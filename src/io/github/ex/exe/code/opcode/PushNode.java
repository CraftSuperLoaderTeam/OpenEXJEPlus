package io.github.ex.exe.code.opcode;

import io.github.ex.exe.core.Executor;
import io.github.ex.exe.obj.ExObject;
import io.github.ex.util.VMRuntimeException;

public class PushNode extends OpNode {
    ExObject obj;
    public PushNode(ExObject obj){
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "push "+obj;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        executor.push(obj);
    }
}

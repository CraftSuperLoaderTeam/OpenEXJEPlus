package io.github.ex.exe.code.opcode;

import io.github.ex.exe.core.Executor;
import io.github.ex.exe.lib.util.ObjectSize;
import io.github.ex.exe.obj.ExBool;
import io.github.ex.exe.obj.ExObject;
import io.github.ex.util.VMRuntimeException;

public class EquNode extends OpNode {
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject obj = ObjectSize.getValue(executor.pop());
        ExObject obj1 = ObjectSize.getValue(executor.pop());

        if(obj.getType()==obj1.getType()){
            executor.push(new ExBool(obj.getData().equals(obj1.getData())));
        }else executor.push(new ExBool(false));
    }
}

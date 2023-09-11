package io.github.ex.exe.code.opcode;

import io.github.ex.exe.core.Executor;
import io.github.ex.exe.lib.util.ObjectSize;
import io.github.ex.exe.obj.ExBool;
import io.github.ex.exe.obj.ExObject;
import io.github.ex.util.VMRuntimeException;

public class AndNode extends OpNode {
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject obj = executor.pop();
        ExObject obj1 = executor.pop();

        obj = ObjectSize.getValue(obj);
        obj1 = ObjectSize.getValue(obj1);

        if(obj1.getType()==ExObject.BOOLEAN&&obj.getType()==ExObject.BOOLEAN){
            executor.push(new ExBool(Boolean.parseBoolean(obj.getData())&&Boolean.parseBoolean(obj1.getData())));
        }else throw new VMRuntimeException("逻辑运算时提取到未知类型",executor.getThread());
    }
}

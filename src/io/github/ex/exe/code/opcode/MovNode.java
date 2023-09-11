package io.github.ex.exe.code.opcode;

import io.github.ex.exe.core.Executor;
import io.github.ex.exe.lib.util.ObjectSize;
import io.github.ex.exe.obj.ExObject;
import io.github.ex.exe.obj.ExValue;
import io.github.ex.util.VMRuntimeException;

public class MovNode extends OpNode {
    @Override
    public void executor(Executor executor) throws VMRuntimeException {

        ExObject o = executor.pop();
        ExObject o1 = executor.pop();

        o = ObjectSize.getValue(o);

        if(o1.getType()!=ExObject.VALUE)throw new VMRuntimeException("The operation type is incorrect",executor.getThread());

        ExValue value = (ExValue) o1;

        if(o.getType()==ExObject.VALUE){
            value.setVar(((ExValue)o).getVar());
            return;
        }

        value.setVar(o);
    }
}

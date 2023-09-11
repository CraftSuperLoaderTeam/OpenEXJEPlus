package io.github.ex.exe.lib;

import io.github.ex.exe.core.Executor;
import io.github.ex.exe.lib.util.ObjectSize;
import io.github.ex.exe.obj.ExObject;
import io.github.ex.util.VMRuntimeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;

public interface RuntimeLibrary {
    interface RuntimeFunction{
        int getVarNum();
        ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException;
        String getName();

        default void exe(Executor executor) throws VMRuntimeException {
            try {
                ArrayList<ExObject> obj = new ArrayList<>();
                for (int i = 0; i < getVarNum(); i++){
                    ExObject o = executor.pop();

                    o = ObjectSize.getValue(o);

                    obj.add(o);
                }
                Collections.reverse(obj);

                executor.push(invoke(obj, executor));
            }catch (EmptyStackException e){
                throw new VMRuntimeException("获取函数形参时发生错误,可能传入参数个数不匹配,实际参数为("+getVarNum()+"个)",executor.getThread());
            }
        }
    }
    ArrayList<RuntimeFunction> functions();
    String getName();
}

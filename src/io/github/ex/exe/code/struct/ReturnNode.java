package io.github.ex.exe.code.struct;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.core.Executor;
import io.github.ex.exe.obj.ExObject;
import io.github.ex.exe.obj.ExValue;
import io.github.ex.exe.obj.ExVarName;
import io.github.ex.exe.thread.ThreadManager;
import io.github.ex.util.ReturnException;
import io.github.ex.util.VMRuntimeException;

import java.util.ArrayList;

public class ReturnNode extends StructNode {
    ArrayList<ASTNode> bcs;
    public ReturnNode(ArrayList<ASTNode> bcs){
        this.bcs = bcs;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {

        for(ASTNode b:bcs)b.executor(executor);

        ExObject o = executor.pop();

        if(o instanceof ExVarName){
            ExValue buf = null;
            for(ExValue v: ThreadManager.getValues()){
                if(v.getData().equals(o.getData())){
                    buf = v;
                    break;
                }
            }
            for(ExValue v:executor.getExecuting().getValues()){
                if(v.getData().equals(o.getData())){
                    buf = v;
                    break;
                }
            }
            if(buf == null)throw new VMRuntimeException("找不到指定变量:"+o.getData(),executor.getThread());

            if(buf.getType()== ExObject.ARRAY){
                o = buf;
            }else o = buf.getVar();
        }

        throw new ReturnException(o);
    }
}

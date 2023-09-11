package io.github.ex.exe.code.struct;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.core.Executor;
import io.github.ex.exe.obj.ExValue;
import io.github.ex.util.VMRuntimeException;

import java.util.ArrayList;

public class LoadVarNode extends StructNode {
    String name;
    int type;
    ArrayList<ASTNode> bcs;
    public LoadVarNode(String name, int type, ArrayList<ASTNode> bcs){
        this.name = name;
        this.bcs = bcs;
        this.type = type;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        for(ASTNode b:bcs)b.executor(executor);



        ExValue v = new ExValue(name,type);
        v.setVar(executor.pop());
        executor.getExecuting().getValues().add(v);
    }

    @Override
    public String toString() {
        return "LOAD:"+bcs;
    }
}

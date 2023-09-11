package io.github.ex.exe.code.struct;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.core.Executor;
import io.github.ex.exe.obj.ExObject;
import io.github.ex.util.VMRuntimeException;

import java.util.ArrayList;

public class IfNode extends StructNode {
    ArrayList<ASTNode> bool,group,else_group;

    public IfNode(ArrayList<ASTNode> bool, ArrayList<ASTNode> group, ArrayList<ASTNode> else_group){
        this.bool = bool;
        this.group = group;
        this.else_group = else_group;
    }
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        for(ASTNode b:bool)b.executor(executor);
        ExObject o = executor.pop();

        if(Boolean.parseBoolean(o.getData())){
            for(ASTNode b:group)b.executor(executor);
        }else {
            for(ASTNode b:else_group)b.executor(executor);
        }
    }
}

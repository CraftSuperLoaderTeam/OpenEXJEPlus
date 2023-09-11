package io.github.ex.exe.code.struct;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.core.Executor;
import io.github.ex.util.VMRuntimeException;

import java.util.ArrayList;

public class BackNode extends StructNode {
    ArrayList<ASTNode> b;
    public BackNode(ArrayList<ASTNode> b){
        this.b = b;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        for(ASTNode bb:b)bb.executor(executor);
    }
}

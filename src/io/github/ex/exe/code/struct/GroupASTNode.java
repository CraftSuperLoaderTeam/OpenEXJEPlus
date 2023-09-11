package io.github.ex.exe.code.struct;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.core.Executor;
import io.github.ex.util.VMRuntimeException;

import java.util.ArrayList;

public class GroupASTNode extends StructNode {
    ArrayList<ASTNode> bc;
    public GroupASTNode(ArrayList<ASTNode> bc){
        this.bc = bc;
    }
    public GroupASTNode(ASTNode bc){
        this.bc = new ArrayList<>();
        this.bc.add(bc);
    }

    @Override
    public String toString() {
        return bc.toString();
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        for(ASTNode bcc:bc)bcc.executor(executor);
    }
}

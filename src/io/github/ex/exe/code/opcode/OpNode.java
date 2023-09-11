package io.github.ex.exe.code.opcode;

import io.github.ex.exe.code.ASTNode;

public abstract class OpNode implements ASTNode {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

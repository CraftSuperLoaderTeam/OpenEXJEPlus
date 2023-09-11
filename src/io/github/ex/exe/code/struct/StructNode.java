package io.github.ex.exe.code.struct;

import io.github.ex.exe.code.ASTNode;

public abstract class StructNode implements ASTNode {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

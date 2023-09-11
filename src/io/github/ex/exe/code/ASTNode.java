package io.github.ex.exe.code;

import io.github.ex.exe.core.Executor;
import io.github.ex.util.VMRuntimeException;

public interface ASTNode {
    public void executor(Executor executor)throws VMRuntimeException;
}

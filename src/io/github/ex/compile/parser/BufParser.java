package io.github.ex.compile.parser;

import io.github.ex.compile.Compiler;
import io.github.ex.exe.code.ASTNode;
import io.github.ex.util.CompileException;

public class BufParser implements BaseParser{
    ASTNode nodes;
    public BufParser(ASTNode nodes){
        this.nodes = nodes;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler) throws CompileException {
        return nodes;
    }
}

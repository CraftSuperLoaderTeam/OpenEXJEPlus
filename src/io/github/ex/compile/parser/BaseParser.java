package io.github.ex.compile.parser;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.compile.Compiler;
import io.github.ex.util.CompileException;

public interface BaseParser {
    public ASTNode eval(Parser parser, Compiler compiler) throws CompileException;


}

package io.github.ex.compile.parser;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.code.struct.WhileNode;
import io.github.ex.compile.Compiler;
import io.github.ex.util.CompileException;

import java.util.ArrayList;

public class WhileParser implements BaseParser{
    ArrayList<ASTNode> bool;
    ArrayList<BaseParser> parsers;

    public WhileParser(ArrayList<ASTNode> bool, ArrayList<BaseParser> parsers){
        this.bool = bool;
        this.parsers = parsers;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler) throws CompileException {
        ArrayList<ASTNode> b = new ArrayList<>();
        for(BaseParser p:parsers)b.add(p.eval(parser,compiler));
        return new WhileNode(bool,b);
    }
}

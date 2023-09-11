package io.github.ex.compile.parser;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.code.struct.GroupASTNode;
import io.github.ex.compile.Compiler;
import io.github.ex.compile.Token;
import io.github.ex.compile.ExpressionParsing;
import io.github.ex.util.CompileException;

import java.util.ArrayList;

public class ExpParser implements BaseParser{
    ArrayList<Token> tds;

    public ExpParser(ArrayList<Token> tds){
        this.tds = tds;
    }
    @Override
    public ASTNode eval(Parser parser, Compiler compiler) throws CompileException {
        ExpressionParsing ep = new ExpressionParsing(tds,parser,compiler);
        return new GroupASTNode(ep.calculate(ep.transitSuffix()));

    }
}

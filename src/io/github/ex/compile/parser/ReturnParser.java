package io.github.ex.compile.parser;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.code.struct.ReturnNode;
import io.github.ex.compile.Compiler;
import io.github.ex.compile.Token;
import io.github.ex.compile.ExpressionParsing;
import io.github.ex.util.CompileException;

import java.util.ArrayList;

public class ReturnParser implements BaseParser{
    ArrayList<Token> tds;
    public ReturnParser(ArrayList<Token> tds){
        this.tds = tds;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler) throws CompileException {
        ExpressionParsing p = new ExpressionParsing(tds,parser,compiler);
        return new ReturnNode(p.calculate(p.transitSuffix()));
    }
}

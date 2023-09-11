package io.github.ex.compile.parser;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.code.struct.BackNode;
import io.github.ex.compile.Compiler;
import io.github.ex.compile.Token;
import io.github.ex.compile.ExpressionParsing;
import io.github.ex.util.CompileException;

import java.util.ArrayList;

public class BackParser implements BaseParser{
    ArrayList<Token> t;
    public BackParser(ArrayList<Token> t){
        this.t = t;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler) throws CompileException {
        ExpressionParsing p = new ExpressionParsing(t,parser,compiler);
        return new BackNode(p.calculate(p.transitSuffix()));
    }
}

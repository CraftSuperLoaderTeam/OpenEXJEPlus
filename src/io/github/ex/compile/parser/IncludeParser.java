package io.github.ex.compile.parser;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.code.struct.NulASTNode;
import io.github.ex.compile.Compiler;
import io.github.ex.compile.Token;
import io.github.ex.util.CompileException;

import java.util.ArrayList;

public class IncludeParser implements BaseParser{
    ArrayList<Token> tds;

    public IncludeParser(ArrayList<Token> tds){
        this.tds = tds;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler)throws CompileException {
        if(tds.size()>2)throw new CompileException("Unable to resolve symbols.",tds.get(tds.size()-2),parser.filename);
        Token l = tds.get(0);
        if(l.getType()==Token.STRING) compiler.getLibnames().add(l.getData());
        else throw new CompileException("Type name is not valid.",l, parser.filename);
        return new NulASTNode();
    }
}

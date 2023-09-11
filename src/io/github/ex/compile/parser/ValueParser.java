package io.github.ex.compile.parser;

import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.code.opcode.PushNode;
import io.github.ex.exe.code.struct.GroupASTNode;
import io.github.ex.exe.code.struct.LoadArrayNode;
import io.github.ex.exe.code.struct.LoadVarNode;
import io.github.ex.exe.obj.ExBool;
import io.github.ex.exe.obj.ExNull;
import io.github.ex.compile.Compiler;
import io.github.ex.compile.Token;
import io.github.ex.compile.ExpressionParsing;
import io.github.ex.util.CompileException;

import java.util.ArrayList;

public class ValueParser implements BaseParser{

    ArrayList<Token> tds;
    private int index = 0;

    private Token getTokens(){
        if(index > tds.size())return null;
        Token td = tds.get(index);
        index += 1;
        return td;
    }


    public ValueParser(ArrayList<Token> tds){
        this.tds = tds;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler) throws CompileException {

        Token name;
        Token td = getTokens();

        if(!(td.getType()==Token.NAME))throw new CompileException("Unable to resolve symbols.",td, parser.filename);
        name = td;
        if(compiler.getValueNames().contains(name.getData()))throw new CompileException("The type "+name.getData()+" is already defined.",td, parser.filename);


        td = getTokens();
        ArrayList<ASTNode> var_bc = new ArrayList<>();

        if(td.getType()==Token.END){
            var_bc.add(new PushNode(new ExNull()));
        }else {
            if (!(td.getType() == Token.SEM))
                throw new CompileException("Unable to resolve symbols.", name, parser.filename);
            td = getTokens();

            if (td.getType() == Token.KEY) {
                switch (td.getData()) {
                    case "true" -> var_bc.add(new PushNode(new ExBool(true)));
                    case "false" -> var_bc.add(new PushNode(new ExBool(false)));
                    case "null" -> var_bc.add(new PushNode(new ExNull()));
                    default -> throw new CompileException("Unable to resolve symbols.", td, parser.filename);
                }
            } else if (td.getType() == Token.END) {
                var_bc.add(new PushNode(new ExNull()));
            }else if(td.getType()==Token.LP){
                boolean isend = true,isf = true;
                ArrayList<GroupASTNode> b = new ArrayList<>();
                do {
                    ArrayList<Token> tds = new ArrayList<>();
                    do {
                        td = getTokens();
                        if (td.getType() == Token.LR && td.getData().equals("]")){
                            isend = false;
                            if(tds.size()==0&&isf){
                                compiler.array_names.add(name.getData());
                                return new LoadArrayNode(name.getData(),new ArrayList<>(),0,0);
                            }
                            break;
                        }
                        if (td.getType() == Token.SEM && td.getData().equals(",")) break;
                        tds.add(td);
                    } while (true);
                    ExpressionParsing p = new ExpressionParsing(tds,parser,compiler);
                    b.add(new GroupASTNode(p.calculate(p.transitSuffix())));
                    isf = false;
                }while (isend);
                compiler.array_names.add(name.getData());
                return new LoadArrayNode(name.getData(),b,0,-1);
            } else {
                ArrayList<Token> t = new ArrayList<>();
                t.add(td);
                do {
                    td = getTokens();
                    if (td.getType() == Token.END) break;
                    t.add(td);
                } while (true);
                ExpressionParsing p = new ExpressionParsing(t, parser, compiler);
                var_bc.addAll(p.calculate(p.transitSuffix()));
            }
        }

        compiler.value_names.add(name.getData());

        return new LoadVarNode(name.getData(),0,var_bc);
    }
}

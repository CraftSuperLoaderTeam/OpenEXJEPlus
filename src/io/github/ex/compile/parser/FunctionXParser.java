package io.github.ex.compile.parser;

import io.github.ex.compile.Compiler;
import io.github.ex.compile.ExpressionParsing;
import io.github.ex.compile.Token;
import io.github.ex.util.CompileException;

import java.io.EOFException;
import java.util.ArrayList;

public class FunctionXParser {
    ArrayList<Token> tds;
    int index;
    Parser parser;
    Compiler compiler;
    Token buffer;

    public FunctionXParser(ArrayList<Token> tds, Parser parser, Compiler compiler) {
        this.tds = tds;
        index = 0;
        this.parser = parser;
        this.compiler = compiler;
    }

    private Token getToken() throws EOFException {
        if (buffer != null) {
            Token r = buffer;
            buffer = null;
            return r;
        } else {
            if (index >= tds.size()) throw new EOFException();
            Token t = tds.get(index);
            index += 1;
            return t;
        }
    }

    private BaseParser getParser() throws EOFException {

        Token buf;
        buf = getToken();
        if (buf.getType() == Token.KEY) {
            switch (buf.getData()) {
                case "value" -> {
                    ArrayList<Token> tds = new ArrayList<>();
                    do {
                        buf = getToken();
                        tds.add(buf);
                    } while (buf.getType() != Token.END);
                    return new BufParser(new ValueParser(tds).eval(parser,compiler));
                }
                case "if" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>(), else_g = new ArrayList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, parser.filename);
                    t = getToken();
                    int index = 1;
                    do {
                        if (t.getType() == Token.LP && t.getData().equals("(")) {
                            vars.add(t);
                            index += 1;
                        }
                        if (t.getType() == Token.LR && t.getData().equals(")") && index > 0) {
                            index -= 1;
                            vars.add(t);
                        }
                        if (t.getType() == Token.LR && t.getData().equals(")") && index <= 0) {
                            for (Token tddebug : vars) {
                                if (tddebug.getType() == Token.NAME || tddebug.getType() == Token.KEY) {
                                    break;
                                }
                            }
                            break;
                        }
                        vars.add(t);
                        t = getToken();
                    } while (true);
                    t = getToken();
                    int i = 1;
                    if (!(t.getType() == Token.LP && t.getData().equals("{")))
                        throw new CompileException("Missing statement body.", t, parser.filename);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);
                    ExpressionParsing e = new ExpressionParsing(vars, parser, compiler);
                    try {
                        t = getToken();
                    } catch (EOFException e11) {
                        return new IfParser(e.calculate(e.transitSuffix()), new Parser.SubParser(groups, parser, compiler,true,false).getParsers(), new ArrayList<>());
                    }
                    int j = 1;
                    if (t.getType() == Token.KEY && t.getData().equals("else")) {
                        t = getToken();
                        if (!(t.getType() == Token.LP && t.getData().equals("{")))
                            throw new CompileException("Missing statement body.", t, parser.filename);
                        do {
                            t = getToken();
                            if (t.getType() == Token.LP && t.getData().equals("{")) j += 1;
                            if (t.getType() == Token.LR && t.getData().equals("}")) j -= 1;
                            if (j == 0) break;
                            else_g.add(t);
                        } while (true);
                    } else buffer = t;

                    return new IfParser(e.calculate(e.transitSuffix()), new Parser.SubParser(groups, parser, compiler,true,false).getParsers(), new Parser.SubParser(else_g, parser, compiler,true,false).getParsers());
                }
                case "while" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, parser.filename);
                    t = getToken();
                    int index = 1;
                    do {
                        if (t.getType() == Token.LP && t.getData().equals("(")) {
                            vars.add(t);
                            index += 1;
                        }
                        if (t.getType() == Token.LR && t.getData().equals(")") && index > 0) {
                            index -= 1;
                            vars.add(t);
                        }
                        if (t.getType() == Token.LR && t.getData().equals(")") && index <= 0) {
                            for (Token tddebug : vars) {
                                if (tddebug.getType() == Token.NAME || tddebug.getType() == Token.KEY) {
                                    break;
                                }
                            }
                            break;
                        }
                        vars.add(t);
                        t = getToken();
                    } while (true);
                    t = getToken();
                    int i = 1;
                    if (!(t.getType() == Token.LP && t.getData().equals("{")))
                        throw new CompileException("Missing statement body.", t, parser.filename);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);
                    ExpressionParsing e = new ExpressionParsing(vars, parser, compiler);
                    return new WhileParser(e.calculate(e.transitSuffix()), new Parser.SubParser(groups, parser, compiler,true,true).getParsers());
                }
                case "back" -> {
                    ArrayList<Token> tds = new ArrayList<>();
                    do {
                        buf = getToken();
                        if (buf.getType() == Token.END) break;
                        tds.add(buf);
                    } while (true);
                    return new BackParser(tds);
                }
                case "return" -> {
                    ArrayList<Token> tds = new ArrayList<>();
                    do {
                        buf = getToken();
                        if (buf.getType() == Token.END) break;
                        tds.add(buf);
                    } while (true);
                    return new ReturnParser(tds);
                }
                default -> throw new CompileException("Not a statement.", buf, parser.filename);
            }
        } else if (buf.getType() == Token.NAME) {
            ArrayList<Token> tds = new ArrayList<>();
            tds.add(buf);
            do {
                buf = getToken();
                tds.add(buf);
            } while (buf.getType() != Token.END);
            if(compiler.getLibnames().contains(buf.getData())){
                return new InvokeParser(tds);
            }
            return new ExpParser(tds);
        } else throw new CompileException("Illegal start of expression.", buf, parser.filename);
    }

    public ArrayList<BaseParser> getParsers() {
        ArrayList<BaseParser> bp = new ArrayList<>();
        try {
            while (true) bp.add(getParser());
        } catch (EOFException e) {
        }
        return bp;
    }
}

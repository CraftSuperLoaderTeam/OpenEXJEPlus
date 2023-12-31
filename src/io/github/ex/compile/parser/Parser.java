package io.github.ex.compile.parser;

import io.github.ex.compile.Compiler;
import io.github.ex.compile.Token;
import io.github.ex.compile.ExpressionParsing;
import io.github.ex.util.CompileException;

import java.util.ArrayList;

public class Parser {
    ArrayList<Token> tds;
    int index;
    Token buffer;
    String filename;

    public String getFilename() {
        return filename;
    }

    public Parser(ArrayList<Token> tds, String filename) {
        this.tds = tds;
        index = 0;
        this.filename = filename;
    }

    private Token getToken() {
        if (buffer != null) {
            Token r = buffer;
            buffer = null;
            return r;
        } else {
            if (index >= tds.size()) return null;
            Token t = tds.get(index);
            index += 1;
            return t;
        }
    }

    public BaseParser getParser(Compiler c) {

        Token buf;
        buf = getToken();
        if (buf == null) return null;
        if (buf.getType() == Token.KEY) {
            switch (buf.getData()) {
                case "include" -> {
                    ArrayList<Token> tds = new ArrayList<>();
                    do {
                        buf = getToken();
                        tds.add(buf);
                    } while (buf.getType() != Token.END);
                    return new IncludeParser(tds);
                }
                case "value" -> {
                    ArrayList<Token> tds = new ArrayList<>();
                    do {
                        buf = getToken();
                        tds.add(buf);
                    } while (buf.getType() != Token.END);
                    return new ValueParser(tds);
                }
                case "function" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();
                    Token t = getToken();
                    if (!(t.getType() == Token.NAME))
                        throw new CompileException("Type name is not valid.", t, getFilename());
                    String name = t.getData();
                    t = getToken();
                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, getFilename());
                    do {
                        t = getToken();
                        if (t.getType() == Token.LR && t.getData().equals(")")) break;
                        if (t.getType() == Token.NAME) c.value_names.add(t.getData());
                        vars.add(t);
                    } while (true);
                    t = getToken();
                    int i = 1;
                    if (!(t.getType() == Token.LP && t.getData().equals("{")))
                        throw new CompileException("Missing function body.", t, getFilename());
                    try {
                        do {
                            t = getToken();
                            if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                            if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                            if (i == 0) break;
                            groups.add(t);
                        } while (true);
                    } catch (NullPointerException e) {
                        throw new CompileException("'}' expected.", filename);
                    }

                    return new FunctionParser(name, vars, new FunctionXParser(groups, this, c).getParsers());

                }
                case "if" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>(), else_g = new ArrayList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, filename);
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
                        if (t.getType() == Token.LR && t.getData().equals(")") && index <= 0) break;

                        vars.add(t);
                        t = getToken();
                    } while (true);
                    t = getToken();
                    int i = 1;
                    if (!(t.getType() == Token.LP && t.getData().equals("{")))
                        throw new CompileException("Missing statement body.", t, getFilename());
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);
                    ExpressionParsing e = new ExpressionParsing(vars, this, c);

                    t = getToken();
                    if (t == null)
                        return new IfParser(e.calculate(e.transitSuffix()), new SubParser(groups, this, c, false,false).getParsers(), new ArrayList<>());

                    int j = 1;
                    if (t.getType() == Token.KEY && t.getData().equals("else")) {
                        t = getToken();
                        if (!(t.getType() == Token.LP && t.getData().equals("{")))
                            throw new CompileException("Missing statement body.", t, getFilename());
                        do {
                            t = getToken();
                            if (t.getType() == Token.LP && t.getData().equals("{")) j += 1;
                            if (t.getType() == Token.LR && t.getData().equals("}")) j -= 1;
                            if (j == 0) break;
                            else_g.add(t);
                        } while (true);
                    } else buffer = t;

                    return new IfParser(e.calculate(e.transitSuffix()), new SubParser(groups, this, c, false,false).getParsers(), new SubParser(else_g, this, c, false,false).getParsers());
                }
                case "while" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, this.filename);
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
                        throw new CompileException("Missing statement body.", t, this.filename);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);
                    ExpressionParsing e = new ExpressionParsing(vars, this, c);
                    return new WhileParser(e.calculate(e.transitSuffix()), new SubParser(groups, this, c, false,true).getParsers());
                }
                default -> throw new CompileException("Not a statement.", buf, getFilename());
            }
        } else if (buf.getType() == Token.NAME) {
            ArrayList<Token> tds = new ArrayList<>();
            tds.add(buf);
            do {
                buf = getToken();
                tds.add(buf);
            } while (buf.getType() != Token.END);
            if(c.getLibnames().contains(buf.getData())){
                return new InvokeParser(tds);
            }
            return new ExpParser(tds);

        } else if (buf.getType() == Token.SEM) {
            return null;
        } else throw new CompileException("Illegal start of expression.", buf, filename);
    }

    public static class SubParser {
        ArrayList<Token> tds;
        int index;
        Parser parser;
        Compiler compiler;
        Token buffer;
        boolean function;
        boolean while_st;

        public SubParser(ArrayList<Token> tds, Parser parser, Compiler compiler, boolean function,boolean while_st) {
            this.tds = tds;
            index = 0;
            this.parser = parser;
            this.compiler = compiler;
            this.function = function;
            this.while_st = while_st;
        }

        private Token getToken() {
            if (buffer != null) {
                Token r = buffer;
                buffer = null;
                return r;
            } else {
                if (index >= tds.size()) return null;
                Token t = tds.get(index);
                index += 1;
                return t;
            }
        }

        private BaseParser getParser() {

            Token buf;
            buf = getToken();
            if (buf == null) return null;
            if (buf.getType() == Token.KEY) {
                switch (buf.getData()) {
                    case "value" -> {
                        ArrayList<Token> tds = new ArrayList<>();
                        do {
                            buf = getToken();
                            tds.add(buf);
                        } while (buf.getType() != Token.END);
                        return new ValueParser(tds);
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

                        t = getToken();
                        if (t == null)
                            return new IfParser(e.calculate(e.transitSuffix()), new SubParser(groups, parser, compiler, function,while_st).getParsers(), new ArrayList<>());

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

                        return new IfParser(e.calculate(e.transitSuffix()), new SubParser(groups, parser, compiler, function,while_st).getParsers(), new SubParser(else_g, parser, compiler, function,while_st).getParsers());
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
                        return new WhileParser(e.calculate(e.transitSuffix()), new SubParser(groups, parser, compiler, function,true).getParsers());
                    }
                    case "break" -> {
                        if(while_st) {
                            ArrayList<Token> tds = new ArrayList<>();
                            do {
                                buf = getToken();
                                if (buf.getType() == Token.END) break;
                                tds.add(buf);
                            } while (true);
                            return new BackParser(tds);
                        }else throw new CompileException("Back outside loop",buf, parser.filename);
                    }
                    case "return" -> {
                        if (function) {
                            ArrayList<Token> tds = new ArrayList<>();
                            do {
                                buf = getToken();
                                if (buf.getType() == Token.END) break;
                                tds.add(buf);
                            } while (true);
                            return new ReturnParser(tds);
                        } else throw new CompileException("Return outside function.", buf, parser.filename);
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
            ArrayList<BaseParser> bp = new ArrayList<>();BaseParser bpp;
            while ((bpp = getParser())!=null) bp.add(bpp);
            return bp;
        }
    }
}

package io.github.ex.compile;

import io.github.ex.CompileManager;
import io.github.ex.exe.code.ASTNode;
import io.github.ex.exe.core.Script;
import io.github.ex.exe.thread.ThreadTask;
import io.github.ex.compile.parser.BaseParser;
import io.github.ex.compile.parser.Parser;
import io.github.ex.util.CompileException;

import java.util.ArrayList;

public class Compiler {
    String filename;
    ArrayList<ASTNode> bcs;
    ArrayList<String> libname;
    public ArrayList<String> value_names;
    public ArrayList<String> array_names;

    public ArrayList<String> getLibnames() {
        return libname;
    }

    public Compiler(String filename) {
        this.filename = filename;
        this.libname = new ArrayList<>();
        this.value_names = new ArrayList<>();
        this.array_names = new ArrayList<>();
        libname.add(filename.split("\\.")[0]);
        this.bcs = new ArrayList<>();
    }

    public ArrayList<String> getValueNames() {
        return value_names;
    }

    public void compile(ThreadTask task) {
        try {
            LexicalAnalysis al = new LexicalAnalysis(CompileManager.getFileData(filename), filename);
            ArrayList<Token> t = new ArrayList<>(), o = new ArrayList<>();
            for (Token b : al.getTokens()) {
                if (b.type == Token.LINE) continue;
                t.add(b);
            }

            for (Token b : t)
                if (b.type != Token.TEXT) o.add(b);

            Parser parser = new Parser(o, al.file_name);

            while (true) {
                BaseParser bp = parser.getParser(this);
                if (bp == null) break;
                bcs.add(bp.eval(parser, this));
            }


            task.addScripts(new Script(filename.split("\\.")[0], filename, bcs));
        }catch (CompileException e){
            e.printStackTrace();
            System.exit(0);
        }
    }
}

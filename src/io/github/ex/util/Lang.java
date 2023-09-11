package io.github.ex.util;

import java.util.HashMap;

public class Lang {
    static HashMap<String,String> lang = new HashMap<>();

    /* need.lr.b '}' expected.
     * need.lp.b '{' expected.
     * need.lp.s '(' expected.
     * need.lr.s ')' expected.
     * need.sem.marks '"' expected.
     * need.sem.call '.'expected.
     * miss.function.body Missing function body.
     * miss.statement.body Missing statement body.
     * type.not.valid Type name is not valid.
     * unable.resolve.symbols Unable to resolve symbols.
     * not.statement Not a statement.
     * illegal.expression.start Illegal start of expression.
     * illegal.expression.comb Illegal combination of expressions.
     * illegal.string.escape Illegal escape character in string literal.
     * illegal.key Illegal keywords.
     * outside.function.return Return outside function.
     * outside.loop.back Back outside loop
     */


    public static void read(){

    }

    public static String format(String format){
        String r = lang.get(format);
        if(r == null) return format;
        return r;
    }
}

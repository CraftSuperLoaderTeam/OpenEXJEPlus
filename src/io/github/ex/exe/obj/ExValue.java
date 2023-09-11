package io.github.ex.exe.obj;

public class ExValue extends ExObject{
    String name;
    int type; // 1 local| 0 global
    ExObject var;
    public ExValue(String name,int type){
        this.name = name;
        this.type = type;
        this.var = new ExNull();
    }

    public ExValue(ExObject object){
        this.var = object;
        this.name = "";
    }

    public ExValue(){
        this.name = "";
    }

    public String getName() {
        return name;
    }

    public void setVar(ExObject var) {
        this.var = var;
    }

    public ExObject getVar() {
        return var;
    }

    @Override
    public String getData() {
        return name;
    }

    @Override
    public int getType() {
        return ExObject.VALUE;
    }

    @Override
    public String toString() {
        return "Value:"+name+"|Var:"+var;
    }
}

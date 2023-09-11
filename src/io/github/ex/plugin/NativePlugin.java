package io.github.ex.plugin;

import io.github.ex.exe.lib.RuntimeLibrary;

import java.util.ArrayList;

public abstract class NativePlugin implements RuntimeLibrary {
    ArrayList<RuntimeFunction> functions;

    public NativePlugin(){
        functions = new ArrayList<>();
    }

    public void registerFunction(RuntimeFunction function){
        this.functions.add(function);
    }

    @Override
    public ArrayList<RuntimeFunction> functions() {
        return functions;
    }
}

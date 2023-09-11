package com.plugin;

import io.github.ex.exe.core.Executor;
import io.github.ex.exe.obj.ExArray;
import io.github.ex.exe.obj.ExObject;
import io.github.ex.plugin.NativePlugin;
import io.github.ex.util.VMRuntimeException;

import java.util.ArrayList;

public class PluginMain extends NativePlugin{
    @Override
    public String getName() {
        return "native";
    }
    public PluginMain(){
        registerFunction(new Printf());
    }
    private static class Printf implements RuntimeFunction{

        @Override
        public int getVarNum() {
            return 1;
        }

        @Override
        public ExObject invoke(ArrayList<ExObject> arrayList, Executor executor) throws VMRuntimeException {
            ExObject o = arrayList.get(0);
            if(o.getType()==ExObject.ARRAY){
                ExArray a = (ExArray) o;
                System.out.println(a);
                return null;
            }
            System.out.println(o.getData());
            return null;
        }

        @Override
        public String getName() {
            return "printf";
        }
    }
}

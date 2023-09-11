package io.github.ex.exe.thread;

import io.github.ex.util.CommandManager;
import io.github.ex.Main;
import io.github.ex.exe.lib.Function;
import io.github.ex.exe.lib.util.NetSocketImp;
import io.github.ex.exe.obj.ExValue;
import io.github.ex.util.CompileException;
import org.java_websocket.enums.ReadyState;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class ThreadManager {
    static ArrayList<ExValue> values = new ArrayList<>();
    static ArrayList<Function> functions = new ArrayList<>();
    static ArrayList<ThreadTask> tasks = new ArrayList<>();
    static NetSocketImp imp;

    public static ArrayList<Function> getFunctions() {
        return functions;
    }

    public static ArrayList<ExValue> getValues() {
        return values;
    }

    public enum Status{
        RUNNING,DEATH,LOADING,WAIT,ERROR
    }
    public static void addThread(ThreadTask task){
        tasks.add(task);
    }
    public static void launch(){

        if(CommandManager.isWebSocket){
            try {
                Main.getOutput().info("[OpenEX]: Connect socket library...");
                Main.getOutput().info("[OpenEX]: IP: localhost:"+CommandManager.port);
                imp = new NetSocketImp(CommandManager.port);
                imp.connect();
                while (!imp.getReadyState().equals(ReadyState.OPEN));
                Main.getOutput().info("[OpenEX]: Connect win!");
            }catch (URISyntaxException e){
                throw new CompileException("ConnectLibraryError: "+e.getLocalizedMessage(),"<web_socket>");
            }
        }

        for(ThreadTask task:tasks){
            if(task.getName().equals("main")){
                task.start();
                return;
            }
        }
    }
}

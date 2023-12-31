package io.github.ex.exe.thread;

import io.github.ex.Main;
import io.github.ex.exe.core.Executor;
import io.github.ex.exe.core.Script;
import io.github.ex.exe.lib.Function;
import io.github.ex.util.VMRuntimeException;

import java.util.ArrayList;
import java.util.EmptyStackException;
public class ThreadTask {
    Executor executor;
    String name;
    Thread thread;
    ThreadManager.Status status;
    ArrayList<Script> scripts;
    Function function;
    public ThreadTask(String name){
        this.name = name;
        this.scripts = new ArrayList<>();
        this.thread = new Thread(() ->{
            try{
                executor.launch();
            }catch (VMRuntimeException e){
                status = ThreadManager.Status.ERROR;
                e.printStackTrace();
            }catch (EmptyStackException e){
                status = ThreadManager.Status.ERROR;
                System.err.println("Error: 堆栈处理错误-在线程["+executor.getThread().name+"]里\n\t" +
                        "Filename: " +executor.getExecuting().getFilename()+"\n\t"+
                        "Version:"+ Main.runtime_version);
                e.printStackTrace();
            }
        });
        this.thread.setName("OpenEX-Executor-"+name);
        this.status = ThreadManager.Status.LOADING;
    }

    public String getFilename(){
        return executor.getExecuting().getFilename();
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void setStatus(ThreadManager.Status status) {
        this.status = status;
    }

    public void addScripts(Script scripts) {
        this.scripts.add(scripts);
    }

    public String getName() {
        return name;
    }

    public void start(){
        if(function==null) {
            this.executor = new Executor(scripts.get(0), this);
            this.status = ThreadManager.Status.RUNNING;
            this.thread.start();
        }else {
            this.status = ThreadManager.Status.RUNNING;
            this.executor = new Executor(new Script(function.getLib(), function.getLib()+".exf",function.getBcs()),this);
            this.thread.start();
        }
    }
}

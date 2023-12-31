package io.github.ex.util;

import io.github.ex.CompileManager;
import io.github.ex.Main;
import io.github.ex.exe.core.LibraryLoader;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    static ArrayList<String> filename = new ArrayList<>();
    public static boolean isWebSocket = false;
    public static int port = 0;
    public static void command(String[] args){

        try {

            OptionParser parser = new OptionParser() {
                {
                    acceptsAll(asList("f","filename")).withRequiredArg()
                            .ofType(String.class)
                            .withValuesSeparatedBy(',')
                            .describedAs("编译一个或多个脚本");
                    acceptsAll(asList("h","help"),"获取帮助");
                    acceptsAll(asList("ws","websocket")).withRequiredArg()
                            .ofType(Integer.class)
                            .describedAs("启用WebSocket通信调用库服务,默认为客户端模式");
                    acceptsAll(asList("l","loadlib")).withRequiredArg()
                            .ofType(String.class)
                            .withValuesSeparatedBy(',')
                            .describedAs("加载一个或多个外部库");
                    acceptsAll(asList("c","compile"),"开启StamonVM编译模式");
                }
            };

            OptionSet set = parser.parse(args);

            if (args.length == 0) {
                Main.getOutput().info("-filename <文件名> //编译一个或多个脚本,中间用','分开\n" +
                        "-loadlib <文件名> //加载一个或多个外部库,中间用','分开\n" +
                        "-websocket <port> //启用WebSocket通信调用库服务 默认为客户端模式\n" +
                        "-compile //开启StamonVM编译模式" +
                        "CompilerVersion:" + Main.compile_version + "\n" +
                        "RuntimeVersion:" + Main.runtime_version);
                return;
            }
            if (set.has("-help")) {
                Main.getOutput().info("-filename <文件名> //编译一个或多个脚本,中间用','分开\n" +
                        "-loadlib <文件名> //加载一个或多个外部库,中间用','分开\n" +
                        "-websocket <port> //启用WebSocket通信调用库服务 默认客户端模式\n" +
                        "-compile //开启StamonVM编译模式" +
                        "CompilerVersion:" + Main.compile_version + "\n" +
                        "RuntimeVersion:" + Main.runtime_version);
                return;
            }

            if (!set.has("filename")) throw new CompileException("请输入-filename命令参数", "<console>");

            filename.addAll((List<String>) set.valuesOf("filename"));

            if (set.has("websocket")) {
                isWebSocket = true;
                port = (Integer) set.valueOf("websocket");
            }

            if(set.has("loadlib")){
                List<String> lib = (List<String>) set.valuesOf("loadlib");
                LibraryLoader.loadLibrary(lib);
            }

            CompileManager.compile(filename);
        }catch (OptionException e){
            throw new CompileException("命令解析发生错误:"+e.getLocalizedMessage(),"<console>");
        }
    }
    private static List<String> asList(String... params) {
        return Arrays.asList(params);
    }
}

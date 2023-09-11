package io.github.ex.exe.core;

import io.github.ex.exe.lib.*;
import io.github.ex.plugin.NativePlugin;
import io.github.ex.util.CompileException;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LibraryLoader {

    static ArrayList<RuntimeLibrary> rls = new ArrayList<>();
    static {
        rls.add(new Sys());
        rls.add(new Array());
        rls.add(new Type());
        rls.add(new File());
        rls.add(new StringLib());
    }

    public static void loadLibrary(List<String> library){
        String filename = "";
        try {
            JarFile jar;java.io.File file;
            JarEntry entry;
            Properties properties;
            for (String f : library) {
                filename = f;
                file = new java.io.File(f);
                jar = new JarFile(file);
                entry = jar.getJarEntry("plugin.cfg");
                properties = new Properties();
                properties.load(jar.getInputStream(entry));

                URL url1 = new URL("file","",slashify(file.getAbsolutePath(),file.isDirectory()));

                URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 }, Thread.currentThread()
                        .getContextClassLoader());
                Class<?> myClass1 = myClassLoader1.loadClass(properties.getProperty("main-class"));

                NativePlugin p = (NativePlugin) myClass1.newInstance();
                rls.add(p);
            }
        }catch (IOException io) {
            throw new CompileException("IOException: " + io.getLocalizedMessage(), filename);
        }catch (ClassCastException e){
            throw new CompileException("Not a plugin: " + e.getLocalizedMessage(), filename);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new CompileException("ReflectiveOperationException: " + e.getLocalizedMessage(), filename);
        }
    }

    private static String slashify(String path, boolean isDirectory) {
        String p = path;
        if (java.io.File.separatorChar != '/')
            p = p.replace(java.io.File.separatorChar, '/');
        if (!p.startsWith("/"))
            p = "/" + p;
        if (!p.endsWith("/") && isDirectory)
            p = p + "/";
        return p;
    }

    public static ArrayList<RuntimeLibrary> getLibs(){
        return rls;
    }
}

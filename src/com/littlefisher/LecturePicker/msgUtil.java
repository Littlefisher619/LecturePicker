package com.littlefisher.LecturePicker;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.PrintStream;

public class msgUtil {
    private static PrintStream out=System.out;
    private static boolean DEBUGMODE=false;
    public static void init(){
        AnsiConsole.systemInstall();
    }

    public static void toggleDebugOn(){
        DEBUGMODE=true;
    }
    public static void toggleDebugOff(){
        DEBUGMODE=false;
    }
    public static void newline(int x){
        for(int i=0;i<x;i++)System.out.println();
    }
    public static void newline(){
        System.out.println();
    }
    public static void info(String s)
    {
        System.out.println("[Info] " + s);
    }
    public static void success(String s)
    {
        System.out.println(Ansi.ansi().fgGreen().a("[Success] ").reset() + s);
    }
    public static void warning(String s)
    {
        System.out.println(Ansi.ansi().fgYellow().a("!!Warning!! ").reset() + s);
    }
    public static void failure(String s)
    {
        System.out.println(Ansi.ansi().fgRed().a("!!Error!! ").reset() + s);
    }
    public static void debug(String s)
    {
        if(!DEBUGMODE)return;
        System.out.println("[Debug] " + s);
    }
    public static void info(String s,Object ...args)
    {
        System.out.printf("[Info] " + s + "\n",args);
    }
    public static void success(String s,Object ...args)
    {
        System.out.printf(Ansi.ansi().fgGreen().a("[Success] ").reset() + s,args);
        newline();
    }
    public static void warning(String s,Object ...args)
    {
        System.out.printf(Ansi.ansi().fgYellow().a("!!Warning!! ").reset() + s,args);
        newline();
    }
    public static void failure(String s,Object ...args)
    {
        System.out.printf(Ansi.ansi().fgRed().a("!!Error!! ").reset() + s,args);
        newline();
    }
    public static void debug(String s,Object ...args)
    {
        if(!DEBUGMODE)return;
        System.out.printf("[Debug] " + s + "\n",args);
    }

}

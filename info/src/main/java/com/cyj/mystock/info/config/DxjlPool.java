package com.cyj.mystock.info.config;

import java.util.concurrent.CopyOnWriteArraySet;

public class DxjlPool {

    private static CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<String>();

    public static boolean contains(String content){
        return set.contains(content);
    }

    public static void clear(){
        set.clear();
    }
    public static void setValue(String content){
        if(set!=null) {
            set.clear();
            set.add(content);
        }
    }
}

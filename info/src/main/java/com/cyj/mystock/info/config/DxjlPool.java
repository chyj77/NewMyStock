package com.cyj.mystock.info.config;

import java.util.ArrayList;
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
    public static String duplicateRemove(String content){
        if(set.size()==0) return content;
        ArrayList<String> oldList = new ArrayList<String>();
        ArrayList<String> newList = new ArrayList<String>();
        if(content.indexOf("</tr>")>-1){
            String[] newContets = content.split("</tr>");
            for(String newContent:newContets){
                newContent = newContent +"</tr>";
                newList.add(newContent);
            }
        }
        String poolContent = set.iterator().next();
        if(poolContent.indexOf("</tr>")>-1){
            String[] oldContets = poolContent.split("</tr>");
            for(String oldContent:oldContets){
                oldContent = oldContent +"</tr>";
                oldList.add(oldContent);
            }
        }
        for(int i=(newList.size()-1);i>-1;){
            for(int j=(oldList.size()-1);j>-1;){
                String oldStr = oldList.get(j);
                String newStr = newList.get(i);
                if(oldStr.equals(newStr)){
                    newList.remove(newStr);
                    oldList.remove(oldStr);
                    i--;
                    j--;
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<newList.size();i++){
            sb.append(newList.get(i));
        }
        return sb.toString();
    }
}

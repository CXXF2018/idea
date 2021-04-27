package com.tcs.common.constant;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigConstant {

    private static Map<String,String> valuMap=new HashMap   <String,String>();

    static {
        ResourceBundle ct = ResourceBundle.getBundle("ct");
        Enumeration<String> enumeration = ct.getKeys();
        while(enumeration.hasMoreElements()){
            String key = enumeration.nextElement();
            String value = ct.getString(key);
            valuMap.put(key,value);
        }
    }

    public static String getVal(String key){
        return valuMap.get(key);
    }

    public static void main(String[] args) {
        System.out.println(ConfigConstant.getVal("table"));
    }
}

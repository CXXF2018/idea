package com.atjava;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

//获取两个字符串中最大相同子串。
public class MaxSubstr {

    public List getMaxSameString(String str1, String str2){

        List strings = new ArrayList<String>();

        String maxStr=(str1.length()>str2.length()) ? str1:str2;
        String minStr=(str1.length()<str2.length()) ? str1:str2;

        int length=minStr.length();
        String copy="";
        for (int i = 0; i <length ; i++) {
            for (int x=0,y=length-i;y<=length;x++,y++) {
                String tmp=minStr.substring(x,y);
                if (maxStr.contains(tmp)) {
                    if(copy==""){
                        copy+=tmp;
                        strings.add(tmp);
                    }
                    else if(tmp.length()>=copy.length()){
                        strings.add(tmp);
                    }
                }
            }
        }
        return strings;
    }

    @Test
    public void test(){
        String str1="asdfczcvxvzxc";
        String str2="vzxcvxzasdfvzxcvzcvzxcvxzasdfvzxcvzc";
        System.out.println(getMaxSameString(str1, str2));
    }
}

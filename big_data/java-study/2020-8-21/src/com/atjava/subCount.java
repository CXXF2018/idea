package com.atjava;

import org.junit.Test;

/*获取一个字符串在另一个字符串中出现的次数。
判断str2在str1中出现的次数获取一个字符串在
另一个字符串中出现的次数。判断str2在str1中出现的次数*/
public class subCount {
    public int getCount(String mainStr,String subStr){
        int index=0;
        int count=0;

        while((index=mainStr.indexOf(subStr,index))!=-1){
            count++;
            index+=subStr.length();
        }

        return count;
    }

    @Test
    public void run(){
        String mainStr="asasasasasasasas";
        String subStr="as";
        System.out.println(getCount(mainStr, subStr));
    }
}

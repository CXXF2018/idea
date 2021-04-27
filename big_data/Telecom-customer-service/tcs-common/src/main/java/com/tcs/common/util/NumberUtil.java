package com.tcs.common.util;


import java.text.DecimalFormat;

//数字的工具类
public class NumberUtil {

    //将数字格式化为字符串
    public static String format(int num, int length){

        StringBuilder stringBuilder =new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append("0");
        }
        DecimalFormat df=new DecimalFormat(stringBuilder.toString());
        return df.format(num);
    }

}

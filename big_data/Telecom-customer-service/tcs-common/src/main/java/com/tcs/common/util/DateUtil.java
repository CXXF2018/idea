package com.tcs.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//日期工具类
public class DateUtil {

//将日期字符串按照指定格式解析为日期对象
    public static Date parse(String dateString,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        Date date= null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

// 日期→string
    public static String format(Date date,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
}

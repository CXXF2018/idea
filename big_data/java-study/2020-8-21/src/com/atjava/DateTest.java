package com.atjava;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
    //练习1 字符串“2020-09-08”转换为java.sql.Date
    @Test
    public void testExer() throws ParseException {
        String brith="2020-08-09";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(brith);
        java.sql.Date date1 = new java.sql.Date(date.getTime());

        System.out.println(date1);

    }





}

package com.atjava;
/**
 * String的使用
 */

import org.junit.Test;
public class StringTest {
/*
1.String:字符串，使用一对“”引起来表示
2.string声明为final，不可以被继承
3.String实现了Serialzable接口，可以被序列化
              comparbale接口，字符串之间可以比较大小
4.String内部定义了final char[] value用于存储字符串数据
5.String表示不可变的字符序列
体现：
    1.当对字符串重新赋值时，需要重新指定内存区域赋值，不能使用原有的value进行赋值
    2.对现有的字符串进行连接操作时，也需要重新指定内存区域赋值，不能使用原有的value进行赋值
    3.当调用String
 */

    @Test
    public void test1(){

        String s1="abc";//字面量的定义方式
        String s2="abc";

        System.out.println(s1 == s2);//比较s1和s2的地址值

        s1="hello";

        System.out.println(s1);
        System.out.println(s2);

    }
}

package com.atjava;

public class SeasonTest {

   static class Season{

       //声明season对象的属性
       private final String SeasonName;
       private final String SeasonDesc;

       //私有化构造器，给对象属性赋值
       private Season(String seasonName, String seasonDesc) {
           SeasonName = seasonName;
           SeasonDesc = seasonDesc;
       }

       //提供多个枚举类的对象
       public static final Season SPING=new Season("春天","春暖花开");
       public static final Season SUMMER=new Season("夏天","dfs");

       public String getSeasonName() {
           return SeasonName;
       }

       public String getSeasonDesc() {
           return SeasonDesc;
       }

       @Override
       public String toString() {
           return "Season{" +
                   "SeasonName='" + SeasonName + '\'' +
                   ", SeasonDesc='" + SeasonDesc + '\'' +
                   '}';
       }
   }

    public static void main(String[] args) {
        System.out.println(Season.SPING);
    }
}

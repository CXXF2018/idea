package com.atjava;

import org.junit.Test;

public class StringReverseTest {

    public String Reverse1(String str,int startIndex,int endIndex){
        if(str!=null){
            char[] arr=str.toCharArray();
            for(int x=startIndex,y=endIndex;x<y;x++,y--){
                char tmp=arr[y];
                arr[y]=arr[x];
                arr[x]=tmp;
            }

            return new String(arr);
        }
        return null;
    }

    public String Reverse2(String str,int startIndex,int endIndex){
        if(str!=null){
            String tmp=str.substring(0,startIndex);
            for(int i=endIndex;i>=startIndex;i--){
                tmp+=str.charAt(i);
            }
            tmp+=str.substring(endIndex);

            return tmp;
        }
        return null;

    }

    public String Reverse3(String str,int startIndex,int endIndex){
        StringBuffer tmp=new StringBuffer(str.length());
        tmp.append(str.substring(0,startIndex));
        for(int i=endIndex;i>=startIndex;i--)
        {
            tmp.append(str.charAt(i));
        }
        tmp.append(str.substring(endIndex));

        return new String(tmp);
    }



    @Test
    public void reverseTest(){
        String str="sdfghjkliuytrecvb";
        String reverse1=Reverse1(str,2,6);
        System.out.println(reverse1);
        String reverse2=Reverse2(str,2,5);
        System.out.println(reverse2);
        String reverse3=Reverse3(str,2,5);
        System.out.println(reverse3);

    }
}

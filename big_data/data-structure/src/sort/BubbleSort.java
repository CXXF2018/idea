package sort;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BubbleSort {
    static int num=80000;
    public static void main(String[] args) {
        int[] array = new int[num];

        for (int i = 0; i < num; i++) {
            array[i] = (int) (Math.random() * num);
        }

        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1Str = simpleDateFormat.format(date1);
        System.out.println("排序前"+date1Str);

        bubbleSort(array);

        Date date2 = new Date();
        String date2Str = simpleDateFormat.format(date2);
        System.out.println("排序后"+date2Str);
    }

    private static void bubbleSort(int[] array) {

        int tmp;
        boolean flag=false;

        for (int i = 0; i <array.length-1 ; i++) {
            for (int j = 0; j < array.length-1-i; j++) {
                if (array[j]>array[j+1]){
                    flag=true;
                    tmp=array[j];
                    array[j]=array[j+1];
                    array[j+1]=tmp;
                }
            }
            if (flag){
                flag=!flag;
            }else{
                break;
            }
        }
    }


}

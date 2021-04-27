package sort;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectSort {

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

        selectSort(array);

        Date date2 = new Date();
        String date2Str = simpleDateFormat.format(date2);
        System.out.println("排序后"+date2Str);
    }

    private static void selectSort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int minIndex=i;
            int min=array[i];
            for (int j = i; j < array.length; j++) {
                if (array[j]<=min){
                    min=array[j];
                    minIndex=j;
                }
            }
            if (minIndex!=i){
                array[minIndex]=array[i];
                array[i]=min;
            }
        }
    }
}

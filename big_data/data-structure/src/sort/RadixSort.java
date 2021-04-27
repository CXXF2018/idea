package sort;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RadixSort {

    static int num=80000;

    public static void main(String[] args) {
        int[] array = new int[num];

        for (int i = 0; i < num; i++) {
            array[i] = (int) (Math.random() * num);
        }

//        System.out.println(Arrays.toString(array));

        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1Str = simpleDateFormat.format(date1);
        System.out.println("排序前"+date1Str);

        redixSort(array);

//        System.out.println(Arrays.toString(array));

        Date date2 = new Date();
        String date2Str = simpleDateFormat.format(date2);
        System.out.println("排序后"+date2Str);
    }

    private static void redixSort(int[] array) {
        int max=array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i]>max){
                max=array[i];
            }
        }

        int maxLength = (max + "").length();

        int[][] buckets = new int[10][array.length];
        int[] count = new int[10];

        for (int i = 0,n=1; i < maxLength; i++,n*=10) {
            for (int j = 0; j < array.length; j++) {
                int digitOfElement = array[j] / n % 10;
                buckets[digitOfElement][count[digitOfElement]]=array[j];
                count[digitOfElement]++;
            }

            int index=0;

            for (int k = 0; k <count.length; k++) {
                if (count[k]!=0){
                    for (int l=0;l<count[k];l++){
                        array[index++]=buckets[k][l];
                    }

                    count[k]=0;
                }

            }
        }
    }
}

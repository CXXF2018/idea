package sort;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MergeSort {


    static int num=8;

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

        int[] temp = new int[array.length];
        mergeSort(array,0,array.length-1,temp);

        System.out.println(Arrays.toString(array));

        Date date2 = new Date();
        String date2Str = simpleDateFormat.format(date2);
        System.out.println("排序后"+date2Str);
    }

    private static void mergeSort(int[] array, int l, int r, int[] temp) {
        if (l<r){
            int mid=(l+r)/2;
            mergeSort(array,l,mid,temp);
            mergeSort(array,mid+1,r,temp);
            merge(array,l,mid,r,temp);
        }
    }

    private static void merge(int[] array, int left,int mid, int right, int[] temp) {
        int i= left;
        int j=mid+1;
        int t=0;

        while(i<=mid&&j<=right){
            if (array[i]<=array[j]){
                temp[t]=array[i];
                t++;
                i++;
            }else {
                temp[t]=array[j];
                t++;
                j++;
            }
        }

        while (i<=mid){
            temp[t]=array[i];
            t++;
            i++;
        }

        while (j<=right){
            temp[t]=array[j];
            t++;
            j++;
        }

        t=0;
        int tmpLeft=left;

        while (tmpLeft<=right){
            array[tmpLeft]=temp[t];
            tmpLeft++;
            t++;
        }
    }
}

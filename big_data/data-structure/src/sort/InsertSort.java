package sort;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertSort {

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

        insertSort(array);

        Date date2 = new Date();
        String date2Str = simpleDateFormat.format(date2);
        System.out.println("排序后"+date2Str);
    }

    private static void insertSort(int[] array) {

        for (int i = 0; i < array.length; i++) {
            int insertIndex=i-1;
            int  insertVal=array[i];

            while(insertIndex>=0&&insertVal<array[insertIndex]){
                array[insertIndex+1]=array[insertIndex];
                insertIndex--;
            }

            if (insertIndex+1!=i){
                array[insertIndex+1]=insertVal;
            }

//            System.out.println(Arrays.toString(array));

        }

    }
}

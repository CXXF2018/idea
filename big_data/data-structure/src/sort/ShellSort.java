package sort;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShellSort {

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

        shellSort(array);

        Date date2 = new Date();
        String date2Str = simpleDateFormat.format(date2);
        System.out.println("排序后"+date2Str);
    }

    private static void shellSort(int[] array) {
        int tmp;

        for (int gap = array.length/2; gap >0; gap/=2) {
            for (int i = gap; i < array.length; i++) {
                for (int j = i-gap; j >=0; j-=gap) {
                    if (array[j]>array[j+gap]){
                        tmp=array[j];
                        array[j]=array[j+gap];
                        array[j+gap]=tmp;
                }
            }
        }
//            System.out.println(Arrays.toString(array));
        }
    }



}

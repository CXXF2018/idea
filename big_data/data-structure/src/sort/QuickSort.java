package sort;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class QuickSort {

    static int num = 8;

    public static void main(String[] args) {
        int[] array = new int[num];

        for (int i = 0; i < num; i++) {
            array[i] = (int) (Math.random() * num);
        }

        System.out.println(Arrays.toString(array));

        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1Str = simpleDateFormat.format(date1);
        System.out.println("排序前" + date1Str);

        quickSort(array, 0, array.length - 1);

        System.out.println(Arrays.toString(array));

        Date date2 = new Date();
        String date2Str = simpleDateFormat.format(date2);
        System.out.println("排序后" + date2Str);
    }

    private static void quickSort(int[] array, int l, int r) {
        int left = l;
        int right = r;
        int mid = (left + right) / 2;
        while (left < right) {
            while (left < right && array[left] < array[mid]) {
                left++;
            }
            while (left < right && array[right] > array[mid]) {
                right--;
            }
            if (left >= right) {
                break;
            }
            int tmp = array[left];
            array[left] = array[right];
            array[right] = tmp;
            if (array[left] == array[right]) {
                left++;
                right--;
            }
            if (left == right) {
                left++;
                right--;
            }
        }
        if (right > l) {
            quickSort(array, l, right);
        }
        if (left < r) {
            quickSort(array, left, r);
        }
    }
}

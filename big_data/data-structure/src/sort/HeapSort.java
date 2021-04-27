package sort;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HeapSort {
    static int num=8000000;

    public static void main(String[] args) {
        int[] array = new int[num];

        for (int i = 0; i < num; i++) {
            array[i] = (int) (Math.random() * num);
        }

//       System.out.println(Arrays.toString(array));

        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1Str = simpleDateFormat.format(date1);
        System.out.println("排序前" + date1Str);

        heapSort(array);
//        System.out.println(Arrays.toString(array));

        Date date2 = new Date();
        String date2Str = simpleDateFormat.format(date2);
        System.out.println("排序后" + date2Str);
    }

    public static void heapSort(int[] array) {
        //构建大顶堆
        for (int i = array.length/2-1; i >=0 ; i--) {
            //从最后一个非叶子节点从下至上，从右至左调整结构
            adjustHeap(array,i,array.length);
        }
        //交换堆顶和末尾元素，调整堆结构
        for (int i = array.length-1; i >0 ; i--) {
            int tmp=array[i];
            array[i]=array[0];
            array[0]=tmp;

            adjustHeap(array,0,i);
        }
    }

    private static void adjustHeap(int[] array, int i, int length) {

        int tmp=array[i];

        for(int k=2*i+1;k<length;k=2*k+1){
            if (k+1<length&&array[k]<array[k+1]){
                k++;
            }

            if (array[k]>tmp){
                array[i]=array[k];
                i=k;
            }else {
                break;
            }
        }

        array[i]=tmp;
    }
}



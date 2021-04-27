package sort;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class sort_8 {
    static int num=8;
    public static void main(String[] args) {
        int[] array = new int[num];
        int[] array1 ={1,4,2,5,7,9,8,6,};

        for (int i = 0; i < num; i++) {
            array[i]= (int)(Math.random()*num);
        }

        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1Str = simpleDateFormat.format(date1);
        System.out.println("排序前时间：" + date1Str);

        System.out.println(Arrays.toString(array));

//        bubbleSort(array);
//        quickSort1(array1,0,array1.length-1);
//        quickSort2(array1,0,array1.length-1);
//        insertSort(array);
//        selectSort(array);
//        radixSort(array);
//        mergeSort(array,0,array.length-1);
//        heapSort(array);
        shellSort(array);

        System.out.println(Arrays.toString(array));

        Date date2 = new Date();
        String date2Str = simpleDateFormat.format(date2);
        System.out.println("排序后时间：" + date2Str);
    }

    private static void bubbleSort(int[] array){
        int tmp;
        int len=array.length;
        boolean flag=false;

        for (int i = 0; i < len-1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (array[j]>array[j+1]){
                    tmp=array[j];
                    array[j]=array[j+1];
                    array[j+1]=tmp;
                    flag=true;
                }
            }
            if (flag){
                flag=!flag;
            }else {
                break;
            }
        }
    }

    private static void quickSort1(int[] array,int left,int right){
        int l=left;
        int r=right;
        int privot=array[(left+right)/2];

        while (l<r){
            while (array[l]<privot){
                l++;
            }
            while (array[r]>privot){
                r--;
            }

            if (l>=r){
                break;
            }

            int tmp=array[l];
            array[l]=array[r];
            array[r]=tmp;

            if (array[l]==privot){
                l++;
            }
            if (array[r]==privot){
                r--;
            }
        }

        if (l==r){
            l++;
            r--;
        }

        if (left<r){
            quickSort1(array,left,r);
        }
        if (right>l){
            quickSort1(array,l,right);
        }
    }
    private static void quickSort2(int[] array,int left,int right) {
       if (left<right){
           int l = left;
           int r = right;
           int tmp = array[left];
           while (l < r) {
               while (l<r&&array[r] >= tmp) {
                   r--;
               }
               if (l < r) {
                   array[l++] = array[r];
               }

               while (l<r&&array[l] < tmp) {
                   l++;
               }
               if (l < r) {
                   array[r--] = array[l];
               }
           }
           array[l] = tmp;
           quickSort2(array, left, l-1);
           quickSort2(array, l+1, right);
       }
    }

    private static void insertSort(int[] array){

        for (int i = 0; i < array.length; i++) {
            int insertIndex=i-1;
            int insertVal=array[i];

            while (insertIndex>=0&&array[insertIndex]>insertVal){
                array[insertIndex+1]=array[insertIndex];
                insertIndex--;
            }

            if (insertIndex+1!=i){
                array[insertIndex+1]=insertVal;
            }
        }
    }

    private static void selectSort(int[] array) {
        for (int i = 0; i <array.length; i++) {
            int minIndex=i;
            int minVal=array[i];
            for (int j = i; j < array.length; j++) {
                if (array[j]<=minVal){
                    minIndex=j;
                    minVal=array[j];
                }
            }

            if (minIndex!=i){
                array[minIndex]=array[i];
                array[i]=minVal;
            }
        }
    }

    private static void radixSort(int[] array){
        int max=array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i]>max){
                max=array[i];
            }
        }
        int maxLength=(max+"").length();

        int[][] bucket=new int[10][array.length];
        int[] count=new int[10];

        for (int i = 0,n=1; i <maxLength ; i++,n*=10) {
            for (int j = 0; j < array.length; j++) {
                int last_num=array[j]/n%10;
                bucket[last_num][count[last_num]]=array[j];
                count[last_num]++;
            }

            int index=0;
            for (int j = 0; j <10; j++) {
                for (int k = 0; k < count[j]; k++) {
                    array[index++]=bucket[j][k];
                }
                count[j]=0;
            }
        }
    }

    private static void mergeSort(int[] array,int l,int r){
        if (l>=r){
            return;
        }
        int mid=(l+r)/2;
        mergeSort(array,l,mid);
        mergeSort(array,mid+1,r);
        merge(array,l,mid,r);
    }

    private static void merge(int[] array, int l, int mid, int r) {
        int i=l;
        int j=mid+1;
        int[] tmp=new int[r-l+1];
        int t=0;
        while(i<=mid&&j<=r){
            if (array[i]<=array[j]){
                tmp[t++]=array[i];
                i++;
            }else {
                tmp[t++]=array[j];
                j++;
            }
        }
        while (i<=mid){
            tmp[t++]=array[i];
            i++;
        }
        while (j<=r){
            tmp[t++]=array[j];
            j++;
        }
        i=l;
        for (int k = 0; k < tmp.length; k++) {
            array[i++]=tmp[k];
        }
    }

    private static void heapSort(int[] array){
        for (int i = array.length / 2 - 1; i >=0 ; i--) {
            adjustHeap(array,i,array.length);
        }

        for (int i = array.length-1; i>=0 ; i--) {
            int tmp=array[0];
            array[0]=array[i];
            array[i]=tmp;
            adjustHeap(array,0,i);
        }
    }

    private static void adjustHeap(int[] array,int i,int length){

        int tmp=array[i];
        for (int j = 2*i+1; j < length; j=2*j+1) {
            if (j+1<length&&array[j]<array[j+1]){
                j++;
            }
            if (array[j]>tmp){
                array[i]=array[j];
                i=j;
            }else {
                break;
            }
        }
        array[i]=tmp;
    }

    private static void shellSort(int[] array){
        for (int gap = array.length/2; gap>0;gap/=2) {
            for (int i = gap; i < array.length ; i++) {
                for (int j = i-gap; j>=0; j-=gap) {
                    if (array[j]>array[j+gap]){
                        int tmp=array[j];
                        array[j]=array[j+gap];
                        array[j+gap]= tmp;
                    }
                }
            }
        }
    }

}


import java.util.*;

public class Main {

    public static void main(String[] args) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int[] arr = new int[]{9,3,6,4,2,5};
        //quickSort(arr,0,arr.length-1);
        //bubbleSort(arr);
        //selectSort(arr);
        //insertSort(arr);
        //mergeSort(arr,0,arr.length-1);
        //heapSort(arr);
        //shellSort(arr);
        int[] leastNumbers = getLeastNumbers(arr, 3);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        for (int leastNumber : leastNumbers) {
            System.out.println(leastNumber);
        }

    }

    public static int[] getLeastNumbers(int[] arr, int k) {
        PriorityQueue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            public int compare(Integer num1,Integer num2){
                return num2-num1;
            }
        });
        for(int i = 0;i<k;i++)
            queue.offer(arr[i]);
        for(int i = k;i<arr.length;i++){
            if (queue.peek() > arr[i]) {
                queue.poll();
                queue.offer(arr[i]);
            }
        }
        int[] res = new int[k];
        for(int i = 0;i<k;i++){
            res[i] = queue.poll();
        }
        return res;


    }

    public static void quickSort(int[] arr,int left,int right){
        if(left>right)
            return;
        int l = left;
        int r = right;
        int tmp = arr[l];
        while(l<r){
            while(l<r&&arr[r]>=tmp){
                r--;
            }
            if(l<r){
                arr[l++] = arr[r];
            }
            while(l<r&&arr[l]<tmp){
                l++;
            }
            if(l<r){
                arr[r--] = arr[l];
            }
        }
        arr[l] = tmp;
        quickSort(arr,left,l-1);
        quickSort(arr,l+1,right);

    }

    public static void bubbleSort(int[] arr){
        boolean flag = false;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length-i-1; j++) {
                if(arr[j]>arr[j+1]){
                    int tmp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = tmp;
                    flag = true;
                }
            }
            if(flag){
                flag=!flag;
            }else{
                break;
            }
        }
    }

    public static void selectSort(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            int selectNum = arr[i];
            int selectIndex = i;
            for (int j = i+1; j < arr.length; j++) {
                if(arr[j]<selectNum){
                    selectIndex = j;
                    selectNum = arr[j];
                }
            }
            arr[selectIndex] = arr[i];
            arr[i] = selectNum;
        }
    }

    public static void insertSort(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            int insertNum = arr[i];
            int insertIndex = i-1;
            while(insertIndex>=0&&insertNum<arr[insertIndex]){
                arr[insertIndex+1] = arr[insertIndex];
                insertIndex--;
            }
            arr[insertIndex+1] = insertNum;
        }
    }

    public static void mergeSort(int[] arr,int l,int r){
        if(l>=r)
            return;
        int mid = (l+r)/2;
        mergeSort(arr,l,mid);
        mergeSort(arr,mid+1,r);
        merge(arr,l,mid,r);
    }

    private static void merge(int[] arr, int l, int mid, int r) {

        int[] tmp = new int[r - l + 1];
        int i = l;
        int j = mid+1;
        int index = 0;
        while(i<=mid&&j<=r){
            if(arr[i]<arr[j])
                tmp[index++] = arr[i++];
            else
                tmp[index++] = arr[j++];
        }
        while(i<=mid)
            tmp[index++] = arr[i++];
        while(j<=r)
            tmp[index++] = arr[j++];
        for (int k = 0; k < tmp.length; k++) {
            arr[l+k] = tmp[k];
        }
    }

    public static void heapSort(int[] arr){
        for (int i = arr.length/2-1; i >=0; i--) {
            adjustHeap(arr,i,arr.length);
        }
        for (int i = arr.length-1; i >=0; i--) {
            int tmp = arr[i];
            arr[i] = arr[0];
            arr[0] = tmp;
            adjustHeap(arr,0,i);
        }
    }

    private static void adjustHeap(int[] arr, int i, int length) {
        int tmp =arr[i];
        for(int j = 2*i+1;j<length;j=j*2+1){
            if(j+1<length&&arr[j+1]>arr[j])
                j++;
            if(arr[j]>tmp){
                arr[i] = arr[j];
                i = j;
            }
        }
        arr[i] = tmp;
    }

    public static void shellSort(int[] arr){
        for (int gap = arr.length/2; gap >0 ; gap/=2) {
            for (int i = gap; i <arr.length;i++) {
                for (int j = i-gap; j >=0; j-=gap) {
                    if(arr[j]>arr[j+gap]){
                        int tmp = arr[j+gap];
                        arr[j+gap] = arr[j];
                        arr[j] = tmp;
                    }
                }
            }
        }
    }
}
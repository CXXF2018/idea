package Search;

import java.util.Arrays;

public class FibonacciSearch {

    public static int maxSize=20;

    public static void main(String[] args) {
        int arr[]= { 1,9,11,91,134, 189 };//没有顺序的数组

        System.out.println("index=" + fibSearch(arr, 189));
    }

    //mid要使用到斐波那契数列，先提前获取一个斐波那契数列
    public static int[] fib(){
        int[] f = new int[maxSize];

        f[0]=1;
        f[1]=1;

        for (int i = 2; i < maxSize; i++) {
            f[i]=f[i-1]+f[i-2];
        }

        return f;
    }

    private static int fibSearch(int[] arr, int key) {
        int low=0;
        int high=arr.length-1;
        int k=0;
        int mid=0;
        int[] f=fib();

        while(high>f[k]-1){
            k++;
        }

        int[] tmp= Arrays.copyOf(arr,f[k]);

        for (int i = high+1; i < tmp.length; i++) {
            tmp[i]=arr[high];
        }

        while(low<high){
            mid=low+f[k]-1;

            if (key<tmp[mid]){
                high=mid-1;
                k--;//在左侧，因而查找上一个fib点
            }else if(key>tmp[mid]){
                low=mid+1;
                k-=2;//在右侧，因而查找上两个fib点，代码第23行
            }else{
                if (mid<high){
                    return mid;
                }
                else{
                    return high;
                }
            }
        }
        return -1;

    }
}

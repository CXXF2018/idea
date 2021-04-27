package Search;

public class BinarySearch {
    public static void main(String[] args) {
        int arr[]= { 1,9,11,91,134, 189 };//没有顺序的数组

        int index = binarySearch(arr,0,arr.length-1, 100);

        if(index==-1) {
            System. out.println("没有找到");
        } else {
            System.out.println("找到，下标为=" + index);
        }
    }

    public static int binarySearch(int[] arr, int left, int right, int value) {

        if (left>right){
            return -1;
        }

        int mid=(left+right)/2;
        int midValue=arr[mid];

        if (value>midValue){
            return binarySearch(arr,mid+1,right,value);
        }else if (value<midValue){
            return binarySearch(arr,left,mid-1,value);
        }else {
            return mid;
        }
    }
}



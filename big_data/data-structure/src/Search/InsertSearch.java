package Search;

public class InsertSearch {
    public static void main(String[] args) {
        int arr[]= { 1,9,11,91,134, 189 };//没有顺序的数组

        int index = insertSearch(arr,0,arr.length-1, 1);

        if(index==-1) {
            System. out.println("没有找到");
        } else {
            System.out.println("找到，下标为=" + index);
        }
    }

    public static int insertSearch(int[] arr, int left, int right, int value) {

        if (left>right||value<arr[0]||value>arr[arr.length-1]){
            return -1;
        }

        int mid=left+(value-arr[left])*(right-left)/(arr[right]-arr[left]);
        int midValue=arr[mid];

        if (value>midValue){
            return insertSearch(arr,mid+1,right,value);
        }else if (value<midValue){
            return insertSearch(arr,left,mid-1,value);
        }else {
            return mid;
        }
    }
}

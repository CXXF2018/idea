package sort;

public class sort_8_again {

    public static void main(String[] args) {

    }

    private static void bubbleSort(int[] array){

        int tmp;
        boolean flag =false;

        for (int i = 0; i < array.length-1; i++) {
            for (int j = 0; j < array.length-i-1; j++) {
                if (array[i]>array[j]){
                    tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;
                    flag = true;
                }
            }
            if (flag){
                flag=!flag;
            }else {
                return;
            }
        }
    }

    private static void quickSort(int[]array,int left,int right){
        int l=left;
        int r=right;
        int mid=(l+r)/2;
        int tmp;

        while (l<r){
            while (l<r&&array[l]<array[mid]){
                l++;
            }
            while (l<r&&array[r]>array[mid]){
                r--;
            }

            if (l>=r){
                break;
            }
            tmp=array[l];
            array[l]=array[r];
            array[r]=tmp;
            if (array[l]==array[mid]){
                l++;
            }
            if (array[r]==array[mid]){
                r--;
            }
        }
        if (l==r){
            l++;
            r--;
        }
        if (left<r){
            quickSort(array,left,r);
        }
        if (right>l){
            quickSort(array,l,right);
        }
    }

    private static void selectSort(int[] array){

        for (int i = 0; i < array.length; i++) {
            int min=array[i] ;
            int minIndex=i;
            for (int j = i+1; j < array.length; j++) {
                if (array[j]<=min){
                    min=array[j];
                    minIndex=j;
                }
            }
            if (i!=minIndex){
                array[minIndex]=array[i];
                array[i]=min;
            }
        }
    }

    private static void insertSort(int[] array){

        for (int i = 0; i < array.length; i++) {
            int insertVal=array[i];
            int insertIndex=i-1;
            while (insertIndex>=0&&array[insertIndex]>insertVal){
                array[insertIndex+1]=array[insertIndex];
                insertIndex--;
            }
            if (insertIndex+1!=i){
                array[insertIndex+1]=insertVal;
            }
        }
    }

    private static void radixSort(int[] array){

        int max=array[0];
        for (int cur : array) {
            if (cur>max){
                max=cur;
            }
        }

        int maxLength = ("" + max).length();
        int[][] buckets = new int[10][maxLength];
        int[] count =new int[10];

        for (int i = 0, n=1; i < maxLength; i++,n*=10) {
            for (int j = 0; j < array.length; j++) {
                int last_num=array[j]/n%10;
                buckets[last_num][count[last_num]] = array[j];
                count[last_num]++;
            }

            int index=0;
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < count[j]; k++) {
                    array[index++] = buckets[j][k];
                }
                count[j]=0;
            }
        }
    }

    private static void shellSort(int[] array){

        for (int gap=array.length/2;gap>=1;gap/=2){
            for (int i = gap; i < array.length; i++) {
                for (int j = i-gap; j>=0 ; j-=gap) {
                    if (array[j]>array[i]){
                        int tmp=array[j+gap];
                        array[j+gap]=array[j];
                        array[j]=tmp;
                    }
                }
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
        int[] tmp = new int[r-l+1];
        int i=l;
        int j=mid+1;
        int t=0;
        while (i<=mid&&j<=r){
            if (array[i]<array[j]){
                tmp[t++] = array[i];
                i++;
            }else {
                tmp[t++] = array[j];
                j++;
            }
        }
        while (i<=mid){
            tmp[t++] = array[i];
            i++;
        }

        while (j<=r){
            tmp[t++] = array[j];
            j++;
        }

        for (int k = 0; k < tmp.length; k++) {
            array[l+k] = tmp[k];
        }
    }

    private static void heapSort(int[] array){
        for (int i = array.length/2-1; i >=0; i--) {
            adjustHeap(array,i,array.length);
        }

        for (int i = array.length-1; i >=0 ; i--) {
            int tmp = array[i];
            array[i] = array[0];
            array[0] = tmp;
            adjustHeap(array,0,i);

        }
    }

    private static void adjustHeap(int[] array,int i,int length){
        int tmp = array[i];
        for (int j = 2*i+1; j < length; j=j*2+1) {
            if (j+1<length&&array[j]<array[j+1]){
                j++;
            }
            if (array[j]>tmp){
                array[i] = array[j];
                i=j;
            }else {
                break;
            }
            array[i] = tmp;
        }
    }
}

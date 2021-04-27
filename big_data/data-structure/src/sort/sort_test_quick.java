package sort;

public class sort_test_quick {

    public static void main(String[] args) {

    }

    private void quickSort(int[] array,int left,int right){
        int l = left;
        int r = right;
        int mid = (l+r)/2;

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
            int tmp = array[l];
            array[l] = array[r];
            array[r] = tmp;

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

    private void bubbleSort(int[] array){

        boolean flag = false;

        for (int i = 0; i < array.length-1; i++) {
            for (int j = 0; j < array.length-1-i; j++) {
                if (array[j]>array[j+1]){
                    int tmp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = tmp;
                    flag = true;
                }
            }
            if (flag==false){
                break;
            }else {
                flag = !flag;
            }
        }
    }

    private void selectSort(int[] array){

        for (int i = 0; i < array.length; i++) {
            int selectIndex = i;
            int selectValue = array[i];
            for (int j = i; j < array.length; j++) {
                if (array[j]<selectValue){
                    selectIndex = j;
                    selectValue = array[j];
                }
            }
            if (selectIndex!=i){
                array[selectIndex] = array[i];
                array[i] = selectValue;
            }
        }
    }

    private void insertSort(int[] array){
        for (int i = 0; i < array.length; i++) {
            int insertIndex = i-1;
            int insertValue = array[i];
            while (insertIndex>=0&&array[insertIndex]>insertValue){
                array[insertIndex+1] = array[insertIndex];
                insertIndex--;
            }
            if (insertIndex+1!=i){
                array[insertIndex+1] = insertValue;
            }
        }
    }

    private void bucketSort(int[] array){
        int max = array[0];
        for (int i : array) {
            if (i>max){
                max = i;
            }
        }

        int length = max + "".length();
        int[][] buckets = new int[10][array.length];
        int[] count = new int[10];

        for (int i=1,n=1;i<=length;i++,n*=10){
            for (int j = 0; j < array.length; j++) {
                int last_num = array[j]/n%10;
                buckets[last_num][count[last_num]] = array[i];
                count[last_num]++;
            }

            int index = 0;
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < count[j]; k++) {
                    array[index++] = buckets[j][k];
                }
                count[j] = 0;
            }
        }
    }

    private void shellSort(int[] array){
        for (int gap=array.length/2;gap>=1;gap/=2){
            for (int i = gap; i < array.length; i++) {
                for (int j = i-gap; j >=0 ; j-=gap) {
                    if (array[j]>array[j+gap]){
                        int tmp = array[j];
                        array[j] = array[j+gap];
                        array[j+gap] = tmp;
                    }
                }
            }
        }
    }

    private void mergeSort(int[] array,int left,int right){

        if (left>=right){
            return;
        }

        int mid = (left+right)/2;
        mergeSort(array,left,mid);
        mergeSort(array,mid+1,right);
        merge(array,left,mid,right);
    }

    private void merge(int[] array,int left,int mid,int right){
        int i = left;
        int j = mid+1;
        int[] tmp = new int[right-left+1];

        int index = 0;
        while (i<=mid&&j<=right){
            if (array[i]<=array[j]){
                tmp[index++] = array[i];
            }else {
                tmp[index++] = array[j];
            }
        }

        while (i<=mid){
            tmp[index++] = array[i++];
        }
        while (j<=right){
            tmp[index++] = array[j++];
        }

        for (int k = 0; k < tmp.length; k++) {
            array[left+k] = tmp[k];
        }
    }

    private void heapSort(int[] array){

        for (int i = array.length/2-1; i >=0; i--) {
            adjustHeap(array,i,array.length);
        }

        for (int i = array.length-1; i >=0; i--) {
            int tmp =array[0];
            array[0] = array[i];
            array[i] = tmp;
            adjustHeap(array,0,i);
        }
    }

    private void adjustHeap(int[] array,int i,int length){

        int tmp =array[i];
        for (int j = 2*i+1; j <array.length ; j=j*2+1) {
            if (array[j+1]>array[j]){
                j++;
            }
            if (array[j]>tmp){
                array[i] = array[j];
                i=j;
            }else {
                break;
            }
        }

        array[i] = tmp;
    }
}



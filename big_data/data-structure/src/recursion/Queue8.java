package recursion;

public class Queue8 {

    int max=8;
    int[] array=new int[8];
    static int count=0;

    public static void main(String[] args) {
        Queue8 queue8 = new Queue8();
        queue8.check(0);

        System.out.println(count);


    }

    public void print(){
        count++;
        for (int i = 0; i < array.length; i++) {
            System.out.printf(array[i]+" ");
        }
        System.out.println();
    }

    //判断第n个皇后是否冲突
    public boolean judge(int n){
        for (int i = 0; i < n; i++) {
            if (array[i]==array[n]||Math.abs(array[i]-array[n])==Math.abs(n-i)){
                //判断是否是同行同列或者同一斜线
                return false;
            }
        }

        return true;
    }

    public void check(int n){
        if (n==max){
            print();
            return;
        }
        for (int i = 0; i < max; i++) {
            array[n]=i;
            if (judge(n)){
                check(n+1);
            }
        }
    }
}

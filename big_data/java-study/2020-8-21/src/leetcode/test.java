package leetcode;

public class test {

    public static void main(String[] args) {

        Solution solution = new Solution();

    }


}
class Solution {
    public int fib(int n) {
        long a = 0;
        int max = Integer.MAX_VALUE;
        int cur1=0;
        int cur2=1;
        int tmp;
        for (int i = 2; i <=n; i++) {
            tmp=cur1;
            cur1=cur2+cur1;
            cur2=cur1;
        }

        return cur1;
    }
}


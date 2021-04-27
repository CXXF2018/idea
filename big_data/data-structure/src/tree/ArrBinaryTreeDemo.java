package tree;

public class ArrBinaryTreeDemo {

    public static void main(String[] args) {
        int[] arr={1,2,3,4,5,6,7,8,9};
        arrBinaryTree arrBinaryTree = new arrBinaryTree(arr);
        arrBinaryTree.preOrder();
    }
}

class arrBinaryTree{

    private int[] arr;

    public arrBinaryTree(int[] arr) {
        this.arr = arr;
    }

    public void preOrder() {
        this.preOrder(0);
    }

    public void preOrder(int index) {

        if (arr==null||arr.length==0){
            System.out.println("数组为空");
        }

        System.out.println(arr[index]);

        if (index*2+1< arr.length){
            preOrder(index*2+1);
        }

        if (index*2+2<arr.length){
            preOrder(index*2+2);
        }
    }
}

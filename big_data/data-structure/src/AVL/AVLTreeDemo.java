package AVL;

public class AVLTreeDemo {
    public static void main(String[] args) {
        int[] arr={10,11,7,6,8,9};
        AVLTree avlTree = new AVLTree();

        for (int i = 0; i < arr.length; i++) {
            avlTree.add(new Node(arr[i]));
        }

        avlTree.infixOrder();

        System.out.println("树的高度"+avlTree.getRoot().height());
        System.out.println("树的左子树高度"+avlTree.getRoot().leftHeight());
        System.out.println("树的右子树高度"+avlTree.getRoot().rightHeight());
        System.out.println("当前的根节点"+avlTree.getRoot());


    }
}

class AVLTree{
    private Node root;

    public Node getRoot() {
        return root;
    }

    public void add(Node node){
        if (root==null){
            root=node;
        }else {
            root.add(node);
        }
    }

    public Node search(int value){
        if (root==null){
            return null;
        }else {
            return root.search(value);
        }
    }

    public Node searchParent(int value){
        if (root==null){
            return null;
        }else {
            return root.searchParent(value);
        }
    }

    public void infixOrder(){
        if (root!=null){
            root.infixOrder();
        }else {
            return;
        }
    }

    public void delNode(int value){
        if (root==null){
            return;
        }else {
            //找到要删除的节点
            Node targetNode = search(value);
            if (targetNode==null){
                return;
            }

            //当二叉树只有一个节点时
            if (root.left==null&&root.right==null){
                root=null;
                return;
            }

            //找父节点
            Node parent = searchParent(value);

            //要删除的是叶子节点
            if (targetNode.left==null&&targetNode.right==null){
                if (parent.left != null&&parent.left.value==value) {
                    parent.left=null;
                }else if(parent.right!=null&&parent.right.value==value){
                    parent.right=null;
                }
            }else if (targetNode.left!=null&targetNode.right!=null){

                //删除有两棵子树的节点
                int minVal=delRightTreeMin(targetNode.right);
                targetNode.value=minVal;

            }else {
                //删除的只有一棵子树的节点
                if (targetNode.left!=null){
                    if (parent!=null){
                        if (parent.left.value==value){
                            parent.left=targetNode.left;
                        }else {
                            parent.right=targetNode.left;
                        }
                    }else {
                        root=targetNode.left;
                    }
                }else {
                    if (parent!=null){
                        if (parent.left.value==value){
                            parent.left=targetNode.right;
                        }else {
                            parent.right=targetNode.right;
                        }
                    }else {
                        root=targetNode.right;
                    }
                }
            }
        }
    }

    private int delRightTreeMin(Node node) {

        Node target=node;
        while(target.left!=null){
            target=target.left;
        }
        delNode(target.value);
        return target.value;
    }
}

class Node{
    int value;
    Node left;
    Node right;

    public Node(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                '}';
    }

    public void add(Node node){
        if (node==null){
            return;
        }
        if (node.value<this.value){
            if (this.left==null){
                this.left=node;
            }else {
                this.left.add(node);
            }
        }else {
            if (this.right==null){
                this.right=node;
            }else {
                this.right.add(node);
            }
        }

        if (rightHeight()-leftHeight()>1){
            if (right!=null&&right.leftHeight()>right.rightHeight()){
                right.rightRotate();
                leftRotate();
            }else {
                leftRotate();
            }
            return;
        }

        if (leftHeight()-rightHeight()>1){
            if (left!=null&&left.leftHeight()<left.rightHeight()){
                left.leftRotate();
                rightRotate();
            }else {
                rightRotate();
            }
        }
    }

    public void infixOrder(){
        if (this.left!=null){
            this.left.infixOrder();
        }
        System.out.println(this);
        if (this.right!=null){
            this.right.infixOrder();
        }
    }

    public Node search(int value){
        if(value==this.value){
            return this;
        }else if(value<this.value){
            if (this.left==null){
                return null;
            }

            return this.left.search(value);
        }else {
            if (this.right==null){
                return null;
            }

            return this.right.search(value);
        }
    }

    public Node searchParent(int value){

        if ((this.left!=null&&this.left.value==value)||(this.right!=null&&this.right.value==value)){
            return this;
        }else {
            if (value<this.value&&this.left!=null){
                return this.left.searchParent(value);
            }else if (value>=this.value&&this.right!=null){
                return this.right.searchParent(value);
            }else {
                return null;
            }
        }
    }

    //以该节点为根节点的树的高度
    public int height(){
        return Math.max(left==null?0:left.height(),right==null?0:right.height())+1;
    }

    //返回左子树的高度
    public int leftHeight(){
        if (left==null){
            return 0;
        }
        return left.height();
    }

    //返回右子树的高度
    public int rightHeight(){
        if (right==null){
            return 0;
        }
        return right.height();
    }

    //左旋方法
    private void leftRotate(){
        //创建一个新的节点
        Node newNode = new Node(value);

        //将新节点的左子树设置为当前节点的左子树
        newNode.left=left;

        //将新节点的右子树设置为当前节点的右子树的左子树
        newNode.right=right.left;

        //将当前节点的值设置为有节点的值
        value=right.value;

        //当前节点的左子树设置为新的节点
        left=newNode;

        right=right.right;
    }

    //右旋方法
    private void rightRotate(){
        Node newNode = new Node(value);
        newNode.right=right;
        newNode.left=left.right;
        value=left.value;
        right=newNode;
        left=left.left;
    }
}

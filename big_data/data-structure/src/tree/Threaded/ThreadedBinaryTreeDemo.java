package tree.Threaded;

public class ThreadedBinaryTreeDemo {

    public static void main(String[] args) {
        HeroNode root =  new HeroNode(1, "tom");
        HeroNode node2 = new HeroNode(3, "jack");
        HeroNode node3 = new HeroNode(6, "smith");
        HeroNode node4 = new HeroNode(8, "mary");
        HeroNode node5 = new HeroNode(10, "king");
        HeroNode node6 = new HeroNode(14, "dim");

        root.setLeft(node2);
        root.setRight(node3);
        node2.setLeft(node4);
        node2.setRight(node5);
        node3.setLeft(node6);

        ThreadedBinaryTree threadedBinaryTree = new ThreadedBinaryTree();
        threadedBinaryTree.setRoot(root);
        threadedBinaryTree.threadedNodes();

        //测试:以10号节点测试
        HeroNode leftNode = node5.getLeft();
        HeroNode rightNode = node5. getRight();
        System.out.println("10号结点的前驱结点是="+ leftNode); //3
        System.out.println("10号结点的后继结点是=" + rightNode);//1

        //threadedBinary Tree.infixOrder0;
        System.out. println("使用线索化的方式遍历线索化二叉树");
        threadedBinaryTree.threadedList();//8, 3, 10,1,14, 6
    }
}

//定义ThreadedBinaryTree实现了线索化功能的二叉树
class ThreadedBinaryTree{
    private HeroNode root;

    private HeroNode pre=null;

    public void setRoot(HeroNode root){
            this.root=root;
        }

        public void preOrder() {
            if (this.root != null) {
                this.root.preOrder();
            }else{
                System.out.println("二叉树为空，无法遍历");
            }
        }

        public void infixOrder() {
            if (this.root != null) {
                this.root.infixOrder();
            }else{
                System.out.println("二叉树为空，无法遍历");
            }
        }

        public void postOrder() {
            if (this.root != null) {
                this.root.postOrder();
            }else{
                System.out.println("二叉树为空，无法遍历");
            }
        }

        public void threadedNodes(){
            threadedNodes(root);
        }

        //对节点进行线索化
    public void threadedNodes(HeroNode node) {

        if (node==null){
            return;
        }
        //线索化左子树
        threadedNodes(node.getLeft());

        //线索化当前节点
        if (node.getLeft()==null){
            node.setLeft(pre);
            node.setLeftType(1);
        }
        if (pre!=null&&pre.getRight()==null){
            pre.setRightType(1);
            pre.setRight(node);
        }

        pre=node;

        //线索化右子树
        threadedNodes(node.getRight());

    }

    public void threadedList() {
        HeroNode cur=root;
        while(cur!=null){
            while (cur.getLeftType()==0){
                cur=cur.getLeft();
            }
            System.out.println(cur);

            while (cur.getRightType()==1){
                cur=cur.getRight();
                System.out.println(cur);
            }

            cur=cur.getRight();
        }
    }
}

class HeroNode  {

    private int no;
    private String name;
    private HeroNode left; //默认null
    private HeroNode right; //默认null

    private int leftType;//为0指向左子树，为1为前驱
    private int rightType;

    public int getLeftType() {
        return leftType;
    }

    public void setLeftType(int leftType) {
        this.leftType = leftType;
    }

    public int getRightType() {
        return rightType;
    }

    public void setRightType(int rightType) {
        this.rightType = rightType;
    }

    public HeroNode(int no, String name) {
        this.no = no;
        this.name = name;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HeroNode getLeft() {
        return left;
    }

    public void setLeft(HeroNode left) {
        this.left = left;
    }

    public HeroNode getRight() {
        return right;
    }

    public void setRight(HeroNode right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "HeroNode{" + "no=" + no + ", name='" + name + '\'' + '}';
    }

    public void preOrder() {
        System.out.println(this);
        if (this.left != null) {
            this.left.preOrder();
        }
        if (this.right != null) {
            this.right.preOrder();
        }
    }

    public void infixOrder() {
        if (this.left != null) {
            this.left.preOrder();
        }
        System.out.println(this);

        if (this.right != null) {
            this.right.preOrder();
        }
    }

    public void postOrder() {
        if (this.left != null) {
            this.left.preOrder();
        }

        if (this.right != null) {
            this.right.preOrder();
        }

        System.out.println(this);
    }
}

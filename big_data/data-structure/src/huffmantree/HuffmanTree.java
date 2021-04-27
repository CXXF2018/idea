package huffmantree;

import java.util.ArrayList;
import java.util.Collections;

public class HuffmanTree {
    public static void main(String[] args) {
        int[] array = {13, 7, 8, 3, 29, 6, 1};

        Node root = createHuffmanTree(array);

        preOrder(root);
    }

    public static void preOrder(Node root) {
        if (root != null) {
            root.preOrder();
        } else {
            System.out.println("是空树，不能遍历~ ");
        }

    }

    public static Node createHuffmanTree(int[] array) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (int value : array) {
            nodes.add(new Node(value));
        }

        while (nodes.size()>1){
            Collections.sort(nodes);
            System.out.println("nodes=" + nodes);

            Node leftNode=nodes.get(0);
            Node rightNode=nodes.get(1);

            Node parent = new Node(leftNode.value+rightNode.value);
            parent.left=leftNode;
            parent.right=rightNode;

            nodes.remove(leftNode);
            nodes.remove(rightNode);
            nodes.add(parent);
        }

        return nodes.get(0);
    }
}

class Node implements Comparable<Node>{

    int value;//权值
    Node left;
    Node right;

    public Node(int value) {
        this.value = value;
    }

    public void preOrder(){
        System.out.println(this);
        if (this.left!=null){
            this.left.preOrder();
        }
        if (this.right!=null){
            this.right.preOrder();
        }
    }

    @Override
    public String toString() {
        return "Node{" + "value=" + value + '}';
    }

    @Override
    public int compareTo(Node o) {
        return this.value-o.value;
    }
}

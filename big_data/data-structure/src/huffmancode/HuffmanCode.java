package huffmancode;

import java.util.*;

public class HuffmanCode {
    public static void main(String[] args) {
        String content="i like like like java do you like a java";
        byte[] contentBytes = content.getBytes();
        System.out.println(contentBytes.length);

        List<Node> nodes = getNodes(contentBytes);
        System.out.println(nodes);

        Node huffmanTreeRoot = createHuffmanTree(nodes);

        huffmanTreeRoot.perOrder();
    }

//1.将赫夫曼编码表存放在Map<Byte,String>形式
    //32->01 97->100 100->11000等形式
    static Map<Byte,String> huffmanCodes=new HashMap<Byte,String>();

    //在生成赫夫曼编码表时，需要去拼接路径，定义一个StringBuilder存储某个叶子节点的路径
    static StringBuilder stringBuilder=new StringBuilder();



    private static List<Node> getNodes(byte[] bytes){
        List<Node> nodes=new ArrayList<>();
        Map<Byte,Integer> counts=new HashMap<>();

        for (byte b : bytes) {
            Integer count=counts.get(b);
            if (count==null){
                counts.put(b,1);
            }else {
                counts.put(b,count+1);
            }
        }

        for (Map.Entry<Byte, Integer> entry : counts.entrySet()) {//遍历map
            nodes.add(new Node(entry.getKey(),entry.getValue()));
        }

        return nodes;
    }

    private static Node createHuffmanTree(List<Node> nodes){

        while (nodes.size()>1){
            Collections.sort(nodes);

            Node leftNode = nodes.get(0);
            Node rightNode = nodes.get(1);

            Node parent = new Node(null,leftNode.weight+rightNode.weight);
            parent.left=leftNode;
            parent.right=rightNode;

            nodes.remove(leftNode);
            nodes.remove(rightNode);
            nodes.add(parent);
        }

        return nodes.get(0);
    }

    public void perOrder(Node node){
        if (node!=null){
            node.perOrder();
        }
    }

}

//创建node，带数据和权值
class Node implements Comparable<Node>{

    Byte data;//数据
    int weight;//权值
    Node left;
    Node right;

    public Node(Byte data, int weight) {
        this.data = data;
        this.weight = weight;
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
        return "Node{" +
                "data=" + data +
                ", weight=" + weight +
                '}';
    }

    @Override
    public int compareTo(Node o) {
        return this.weight-o.weight;
    }

    public void perOrder(){
        System.out.println(this);

        if (this.left!=null){
            this.left.perOrder();
        }

        if (this.right!=null){
            this.right.perOrder();
        }
    }
}

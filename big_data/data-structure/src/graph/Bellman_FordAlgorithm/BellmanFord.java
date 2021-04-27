package graph.Bellman_FordAlgorithm;


import java.util.Scanner;

public class BellmanFord {

    public static int Max_Value = 100000;

    //创建一个内部类，表示图的一条加权边
    static class edge{
        public int source;
        public int target;
        public int weight;

        edge(int source,int target,int weight){
            this.source=source;
            this.target=target;
            this.weight=weight;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入图的顶点数和边数：");
        int vertex = scanner.nextInt();
        int edges = scanner.nextInt();

        edge[] Edge = new edge[edges];
        System.out.println("请输入具体的边的source target weight:");
        for (int i = 0; i < edges; i++) {
            int source = scanner.nextInt();
            int target = scanner.nextInt();
            int weight = scanner.nextInt();
            Edge[i]=new edge(source,target,weight);
        }

        //调用算法
        bellmanFord(vertex,Edge);

    }

    //返回第0个顶点到其他所有顶点之间的最短距离
    public static void bellmanFord(int n, edge[] Edge){
        int[] shortest = new int[n];

        //初始化最短距离
        for (int i = 1; i < n; i++) {
            shortest[i]=Max_Value;
        }

        for (int i = 1; i < n; i++) {//循环次数为n-1是因为，n个顶点中任意两节点的最短路径至多包含n-1条边
            for (int j = 0; j < Edge.length; j++) {
                if(shortest[Edge[j].target]>shortest[Edge[j].source]+Edge[j].weight){
                    shortest[Edge[j].target]=shortest[Edge[j].source]+Edge[j].weight;
                }
            }
        }

        //判断图中是否有环
        boolean judge = false;
        for (int i = 1; i < n; i++) {
            if (shortest[Edge[i].target]>shortest[Edge[i].source]+Edge[i].weight){
                judge = true;
                System.out.println("图中有环");
                return;
            }
        }
        for (int i = 0; i < shortest.length; i++) {
            System.out.println("0号顶点到"+i+"号顶点的距离为："+shortest[i]);
        }

    }
}

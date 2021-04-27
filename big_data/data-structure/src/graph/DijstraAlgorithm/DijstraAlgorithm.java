package graph.DijstraAlgorithm;

import java.util.Scanner;

public class DijstraAlgorithm {
   public static int Max_Value = 100000;//不能设置为Integer的最大值，防止数组越界

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入图的顶点数和边数：");
        int vertex = scanner.nextInt();
        int edge = scanner.nextInt();

        int[][] martix = new int[vertex][vertex];

        //初始化图的关系矩阵
        for (int i = 0; i < vertex; i++) {
            for (int j = 0; j < vertex; j++) {
                martix[i][j] = Max_Value;
            }
        }
        //配置相应的边信息
        for (int i = 0; i < edge; i++) {
            System.out.println("第"+(i+1)+"条边及其权值：");
            int source = scanner.nextInt();
            int target = scanner.nextInt();
            int weight = scanner.nextInt();
            martix[source][target] = weight;
        }

        System.out.println("请输入出发点的序号（此序号应该大于0，小于"+vertex+":");
        int source = scanner.nextInt();
        dijstra(source,martix);

    }

    private static void dijstra(int source, int[][] martix) {
        //最短路径集合
        int[] shortest = new int[martix.length];
        //标记已经找到最短路径
        int[] visited = new int[martix.length];
        //记录到达各节点最短路径的路径长度
        String[] path = new String[martix.length];
        for (int i = 0; i < martix.length; i++) {
            path[i] = new String(source+"->"+i);
        }

        for (int i = 1; i < martix.length; i++) {
            int min = Integer.MAX_VALUE;
            int index = -1;

            for (int j = 0; j < martix.length; j++) {
                if(visited[j]==0&&martix[source][j]<min){
                    min = martix[source][j];
                    index = j;
                }
            }

            //更新到本次遍历过程中的最短路径
            visited[index] = 1;
            shortest[index] = min;

            //更新通过index节点到达其他节点的路径长度
            for (int j = 0; j < martix.length; j++) {
                if (visited[j]==0&&martix[source][index]+martix[index][j]<martix[source][j]){
                    martix[source][j] = martix[source][index]+martix[index][j];
                    path[j] = path[index]+"->"+j;
                }
            }
        }

        for (int i = 0; i < path.length; i++) {
            if (i!=source){
                if(shortest[i]<Max_Value){
                    System.out.println(source+"->"+i+"的路径为："+path[i]);
                }else {
                    System.out.println(source+"不能达到"+i);
                }
            }
        }
    }
}
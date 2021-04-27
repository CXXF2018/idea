package graph.disAndCost;

import java.util.Scanner;

public class shortestAndCost {

    public static int Max_Val = 100000;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入顶点数n和边数m:");
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[][][] matrix = new int[n][n][2];

        //初始化图矩阵
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(i==j){
                    matrix[i][j][0] = 0;
                    matrix[i][j][1] = 0;
                }else {
                    matrix[i][i][0] = Max_Val;
                    matrix[i][i][1] = Max_Val;
                }
            }
        }

        System.out.println("请输入个顶点对应的关系");
        for (int i = 0; i < m; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            matrix[a][b][0] = scanner.nextInt();
            matrix[a][b][1] = scanner.nextInt();
        }

        System.out.println("请输入起点s和终点t");
        int s = scanner.nextInt();
        int t = scanner.nextInt();

        int[][] shortest = new int[n][2];
        for (int i = 0; i < n; i++) {
            shortest[i][0] = matrix[s][i][0];
            shortest[i][1] = matrix[s][i][1];
        }
        shortest[s][0] = 0;
        shortest[s][1] = 0;
        dijstra(matrix,shortest,s);
        for (int i = 0; i < n; i++) {
            System.out.println(s+"->"+i+"的距离是"+shortest[i][0]+"|"+s+"->"+i+"的花费是"+shortest[i][1]);
        }
    }

    public static void dijstra(int[][][] martix,int[][] shrotest,int s){

        int[] visited = new int[martix.length];

        int k =-1;  //标记最短路径
        visited[s] = 1;

        for (int i = 0; i < martix.length; i++) {
            int path_shortest = Max_Val;
            int cast_shortest = Max_Val;

            for (int j = 0; j < martix.length; j++) {
                if(visited[j]==0&&shrotest[j][0]==path_shortest&&shrotest[j][1]<cast_shortest){
                    cast_shortest = shrotest[j][1];
                    k = j;
                }else{
                    path_shortest = shrotest[j][0];
                    cast_shortest = shrotest[j][1];
                    k = j;
                }
            }
            if(path_shortest == Max_Val){
                break;
            }
            visited[k]=1;
            for (int j = 0; j < martix.length; j++) {
                if(visited[j]==0&&shrotest[j][0]>path_shortest+martix[k][j][0]&&cast_shortest+martix
                        [k][j][1]<Max_Val){
                    shrotest[j][0] = path_shortest+martix[k][j][0];
                    shrotest[j][1] = cast_shortest+martix[k][j][1];
                }else if(visited[j]==0&&shrotest[j][0]==shrotest[k][0]+martix[k][j][0]&&shrotest[j][1] >
                        shrotest[k][1]+martix[k][j][1]){
                    shrotest[j][1] = shrotest[k][1]+martix[k][j][1];
                }
            }
        }
    }
}

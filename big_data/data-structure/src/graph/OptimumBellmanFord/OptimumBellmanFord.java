package graph.OptimumBellmanFord;

import java.util.Scanner;

public class OptimumBellmanFord {

    public static int  Max_Value = 100000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入顶点数和边数：");
        int vertex = scanner.nextInt();
        int edge = scanner.nextInt();

        int[] queue = new int[vertex*edge];//用于存放顶点的队列
        int head=0;
        int tail=0; //队列的头尾指针

        //顶点是否在queue中的标记
        boolean[] books = new boolean[vertex];

        //最短路径矩阵
        int[] dis = new int[vertex];

        int[] fromVertex = new int[edge];
        int[] toVertex = new int[edge];
        int[] edgeWeight = new int[edge];
        //临界存储表
        int[] first = new int[vertex];//数组的1-n号单元格存储1-n号顶点的第一条边的编号，初始化的时候没有边为-1
        int[] next = new int[edge];//编号为i的边的下一条边

        for (int i = 0; i < vertex; i++) {
            dis[i]=Max_Value;
            first[i] = -1;//-1表示当前顶点没有边
        }
        dis[0]=0;//出发点，所以为0

        System.out.println("请输入各边对应的fromVertex toVertex edgeWeight:");
        for (int i = 0; i < edge; i++) {
            fromVertex[i] = scanner.nextInt();
            toVertex[i] = scanner.nextInt();
            edgeWeight[i] = scanner.nextInt();

            //建立邻接表
            next[i] = first[fromVertex[i]];//将现有的第一条边移到当前边的next
            first[fromVertex[i]] = i;//把当前边变成第一条边
        }

        queue[tail++]=0;//0号顶点进入队列
        books[0]=true;

        while(head<tail){//队列不为空
            int tmp = first[queue[head]];//处理队首顶点
            while(tmp!=-1){//扫描当前顶点所有的边
                if(dis[toVertex[tmp]]>dis[fromVertex[tmp]]+edgeWeight[tmp]){//判断是否松弛成功
                    dis[toVertex[tmp]]=dis[fromVertex[tmp]]+edgeWeight[tmp];//更新顶点0到顶点totarget的路程

                    if(!books[toVertex[tmp]]){
                        queue[tail++] = toVertex[tmp];
                        books[toVertex[tmp]] = true;//标记当前的节点进入队列
                    }
                }
                tmp=next[tmp];
            }

            //出队
            books[queue[head++]]=false;
        }

        for (int i = 0; i < vertex; i++) {
            System.out.println("0号顶点到"+i+"号顶点的距离为："+dis[i]);
        }


    }
}

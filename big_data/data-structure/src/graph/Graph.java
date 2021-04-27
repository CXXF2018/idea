package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Graph {

    private ArrayList<String> vertexList;//存储顶点集合
    int[][] edges;
    private int numofEdges;
    private boolean[] isVisited;

    public static void main(String[] args) {
        int n = 5;
        String[] Vertexs = {"A", "B", "C", "D", "E"};
        Graph graph = new Graph(5);
        for (String vertex : Vertexs) {
            graph.insertVertex(vertex);
        }

        graph.insertEdges(0, 1, 1);
        graph.insertEdges(0, 2, 1);
        graph.insertEdges(1, 2, 1);
        graph.insertEdges(1, 3, 1);
        graph.insertEdges(0, 4, 1);

        graph.showGraph();
        graph.dfs();
        System.out.println();
        graph.bfs();
    }

    public Graph(int n) {
        //n代表节点数
        //初始化矩阵和vertexList

        edges = new int[n][n];
        vertexList = new ArrayList<String>();
        numofEdges = 0;
    }

    public void insertVertex(String vertex) {
        vertexList.add(vertex);
    }

    public void insertEdges(int v1, int v2, int weight) {

        edges[v1][v2] = weight;
        edges[v2][v1] = weight;
        numofEdges++;
    }

    public int getNumofVertex() {
        return vertexList.size();
    }

    public int getNumofEdges() {
        return numofEdges;
    }

    public String getValueByIndex(int i) {
        return vertexList.get(i);
    }

    public int getWeight(int v1, int v2) {
        return edges[v1][v2];
    }

    public void showGraph() {
        for (int[] link : edges) {
            String string = Arrays.toString(link);
            System.out.println(string);
        }
    }

    //第一个邻节点下标
    public int getFirstNeighbor(int index) {
        for (int i = 0; i < vertexList.size(); i++) {
            if (edges[index][i] > 0) {
                return i;
            }
        }
        return -1;
    }

    //根据当前邻接点的下标来获取下一个邻接点的下标
    public int getNextNeighbor(int v1, int v2) {
        for (int i = v2 + 1; i < vertexList.size(); i++) {
            if (edges[v1][i] > 0) {
                return i;
            }
        }
        return -1;
    }

    //深度优先遍历
    public void dfs(boolean[] isVisited, int i) {//需要知道该节点是否被访问
        System.out.printf(getValueByIndex(i) + "->");
        isVisited[i] = true;

        //查找第一个邻接节点
        int w = getFirstNeighbor(i);
        while (w != -1) {
            if (isVisited[w] != true) {
                dfs(isVisited, w);
            }
            w = getNextNeighbor(i, w);
        }

    }

    public void dfs() {//遍历所有的节点进行dfs
        isVisited = new boolean[5];
        for (int i = 0; i < getNumofVertex(); i++) {
            if (isVisited[i] != true){
                dfs(isVisited,i);
            }
        }
    }

    public void bfs(boolean[] isVisited,int i){
        int u;//队头节点对应的下标
        int w;//邻接点w
        LinkedList queue = new LinkedList();

        System.out.printf(getValueByIndex(i) + "->");
        isVisited[i]= true;

        queue.addLast(i);

        while (!queue.isEmpty()){
            u=(Integer)queue.removeFirst();
            w=getFirstNeighbor(u);
            while (w!=-1){
                if (isVisited[w]!=true){
                    System.out.printf(getValueByIndex(w) + "->");
                    isVisited[w]=true;
                    queue.addLast(w);
                }

                w=getNextNeighbor(u,w);
            }
        }
    }

    public void bfs(){
        isVisited = new boolean[5];
        for (int i = 0; i < getNumofVertex(); i++) {
            if (!isVisited[i]){
                bfs(isVisited,i);
            }
        }
    }
}
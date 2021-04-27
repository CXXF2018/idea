package graph.FloydAlgorithm;

public class Floyd {
    public static void main(String[] args) {
        int[][] matrix =  {
                {0,-1,3,-1},
                {2,0,-1,-1},
                {-1,7,0,1},
                {6,-1,-1,0}
        };
        System.out.println("实现算法之前：");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
        floyd(matrix);

        System.out.println("实现算法之后：");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
    }

    public static void floyd(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix.length; k++) {
                    if(matrix[j][i]!=-1&&matrix[i][k]!=-1){
                        if(matrix[j][k]>matrix[j][i]+matrix[i][k]||matrix[j][k]==-1){
                            matrix[j][k] = matrix[j][i]+matrix[i][k];
                        }
                    }

                }
            }
        }
    }
}

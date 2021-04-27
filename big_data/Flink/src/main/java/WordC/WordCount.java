package WordC;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

//批处理wordcount程序
public class WordCount {
    public static void main(String[] args) throws Exception {
        //1.创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //2.从文件中读取数据
        String inputPath = "E:\\IdeaProjects\\big_data\\Flink\\src\\main\\resources\\hello.txt";
        DataSource<String> inputDataSet = env.readTextFile(inputPath);

        //3.对数据集进行处理，按空格分词展开，转换成（word，1）二元组进行统计,0表示groupBy的参数位置（单词的位置）
        DataSet<Tuple2<String,Integer>> resultSet =  inputDataSet.flatMap(new MyFlatMapper())
                .groupBy
                (0).sum
                (1);
        //分组后将第二个位置上的数据求和

        resultSet.print();
    }

    //自定义类，实现flatMapFuncton接口，flatMap接受的是实现了flatMapFunction接口的类
    //Tuple2是Flink提供的二元组类型
    public static class MyFlatMapper implements FlatMapFunction<String,Tuple2<String,Integer>>{

        public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
            //按空格分词
            String[] word = s.split(" ");

            //转换成二元组
            for (String w : word) {
                Tuple2<String, Integer> tuple2 = new Tuple2<String, Integer>(w, 1);
                collector.collect(tuple2);//收集数据
            }
        }
    }
}

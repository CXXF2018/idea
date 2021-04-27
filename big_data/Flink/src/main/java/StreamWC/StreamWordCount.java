package StreamWC;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class StreamWordCount {

    public static void main(String[] args) throws Exception {
        //创建流处理环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        /*String inputPath= "E:\\IdeaProjects\\big_data\\Flink\\src\\main\\resources\\hello.txt";
        DataStreamSource<String> stringDataStream = env.readTextFile(inputPath);*/

        //用Parameter tool工具从程序启动参数中提取配置项
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");

        //从socket文本流中读取数据
        DataStream<String> stringDataStream = env.socketTextStream(host, port);

        //基于数据流进行转换计算,流会不停的有数据，所以不可以用groupBy，而是用keyBy
        DataStream<Tuple2<String, Integer>> resultStream = stringDataStream
                .flatMap(new MyFlatMapper()).keyBy(0).sum(1).setParallelism(2).slotSharingGroup
                        ("dangqianzu");//组内的任务共享一个slot

        resultStream.print().setParallelism(1);//只写到这然后直接运行是不会出效果的

        //执行任务
        env.execute();//观察结果可以发现是并行计算

    }

    //自定义类，实现flatMapFuncton接口，flatMap接受的是实现了flatMapFunction接口的类
    //Tuple2是Flink提供的二元组类型
    public static class MyFlatMapper implements FlatMapFunction<String,Tuple2<String,Integer>> {

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

package APITest.Transform;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class base {
    public static void main(String[] args) throws Exception {
        //创建流处理环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        String inputPath= "E:\\IdeaProjects\\big_data\\Flink\\src\\main\\resources\\hello.txt";
        DataStreamSource<String> stringDataStream = env.readTextFile(inputPath);

        //1.map算子，本例将字符串转换为字符串的长度
        stringDataStream.map(new MapFunction<String, Integer>() {

            public Integer map(String s) throws Exception {
                return s.length();
            }
        });

        //2,flatMap算子，按空格切分字段
        DataStream<String> dataStream = stringDataStream.flatMap(new FlatMapFunction<String, String>() {
            public void flatMap(String s, Collector<String> collector) throws Exception {
                String[] values = s.split(" ");
                for (String value : values) {
                    collector.collect(value);
                }
            }
        });

        //3.filter 筛选sensor_1开头的Id对应的数据
        stringDataStream.filter(new FilterFunction<String>() {
            public boolean filter(String s) throws Exception {
                return s.startsWith("Sensor_1");
            }
          });

        //打印输出


        //执行
        env.execute();
    }
}

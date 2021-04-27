package APITest.Source;

import APITest.Beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.HashMap;
import java.util.Random;

public class Test_UDF {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //从集合中读取数据
        DataStreamSource<SensorReading> sensorReadingDataStream = env.addSource(new
                MySensorSource());

         //打印数据
        sensorReadingDataStream.print();

        env.execute();
    }

    private static class MySensorSource implements SourceFunction<SensorReading>{

        //定义一个标志位用于cancel控制run方法
        private boolean running = true;

        public void run(SourceContext<SensorReading> sourceContext) throws Exception {
            //定义一个随机数发生器
            Random random = new Random();

            HashMap<String, Double> SensorTempMap = new HashMap<String, Double>();
            for (int i = 0; i < 10; i++) {
                SensorTempMap.put("sensor_"+(i+1),random.nextGaussian()*20+60);
            }

            while(running){
                for (String key : SensorTempMap.keySet()) {
                    double nextTemp = SensorTempMap.get(key) + random.nextGaussian();
                    SensorTempMap.put(key, nextTemp);
                    SensorReading sensorReading = new SensorReading(key, System.currentTimeMillis(), nextTemp);
                    sourceContext.collect(sensorReading);
                }
                Thread.sleep(1000L);
            }
        }

        public void cancel() {
            running = false;
        }
    }
}

package Producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class CustomProducer{


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();

        //kafka集群，broker-list
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop01:9092");
        properties.put(ProducerConfig.ACKS_CONFIG,"all");

        //重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG,1);

        //批次大小
        properties.put("batch.size",16384);

        //等待时间
        properties.put("linger.ms",1);

        //RecordAccumulator缓冲区大小
        properties.put("buffer.memory",33554432);

        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        //创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        //发送数据
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>("first", "atguigu--" + i));
        }

        //关闭资源
        producer.close();
    }
}

package Consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

public class CustomConsumer {

    public static Map<TopicPartition, Long> currentOffest = new HashMap<TopicPartition, Long>();

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop01:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"test");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");

        final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        //订阅主题
        consumer.subscribe(Arrays.asList("first"), new ConsumerRebalanceListener() {

            //该方法在rebalance之前调用
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                commitOffset(currentOffest);

            }

            //该方法在rebalance之后调用
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                currentOffest.clear();
                for (TopicPartition partition : collection) {
                    consumer.seek(partition,getOffset(partition));//定位到最近提交的offset位置继续消费
                }
            }
        });

        while (true){
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset=%d,key=%s,value=%s%n",record.offset(),record.key(),
                        record.value());
                currentOffest.put(new TopicPartition(record.topic(),record.partition()),record
                        .offset());
            }

            //异步提交
            commitOffset(currentOffest);
        }
    }

    //获取某个分区最新的offset
    private static long getOffset(TopicPartition partition){
        return 0;
    }

    //提交该消费者所有分区的offset
    private static void commitOffset(Map<TopicPartition,Long> currentOffest){

    }
}

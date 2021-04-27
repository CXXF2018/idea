package com.tcs.consumer.bean;

import com.tcs.common.bean.Consumer;
import com.tcs.common.constant.Names;
import com.tcs.consumer.dao.HbaseDao;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

//通话日志的消费者对象
public class CalllogConsumer implements Consumer {

    //消费数据
    public void consume() {

        try {
            //创建配置对象
            Properties properties = new Properties();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("consumer.properties"));

            //获取flume采集的数据
            KafkaConsumer kafkaConsumer = new KafkaConsumer<String,String>(properties);

            //关注主题
            kafkaConsumer.subscribe(Arrays.asList(Names.TOPIC.getvalue()));

            HbaseDao hbaseDao = new HbaseDao();

            //初始化
            hbaseDao.init();

            //消费数据
            while (true){
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);

                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {

                    System.out.println(consumerRecord.value());

                    //插入数据
                   hbaseDao.insertData(consumerRecord.value());
                    //Calllog log = new Calllog(consumerRecord.value());
                    //hbaseDao.insertData(log);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //关闭资源
    public void close() throws IOException {

    }
}

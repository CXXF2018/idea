package com.tcs.consumer;


import com.tcs.consumer.bean.CalllogConsumer;

import java.io.IOException;

//启动消费者
public class Bootstrap {

    public static void main(String[] args) throws IOException {

        //创建消费者
        CalllogConsumer calllogConsumer = new CalllogConsumer();

        //使用kafka消费flume采集的数据
        calllogConsumer.consume();

        //将采集到的数据存储到Hbase中去
        calllogConsumer.close();
    }
}

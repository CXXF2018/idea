package com.tcs.producer;

import com.tcs.common.bean.Producer;
import com.tcs.producer.bean.LocalFileProducer;
import com.tcs.producer.io.LocalFileDataIn;
import com.tcs.producer.io.LocalFileDataOut;

import java.io.IOException;

/**
 * 启动对象
 */

public class Bootstrap {
    public static void main(String[] args) throws IOException {

       if(args.length<2){
            System.out.println("系统参数不正确");
            System.exit(1);
        }

        //构建生产者对象
        Producer producer=new LocalFileProducer();

       /* producer.setIn(new LocalFileDataIn("E:\\big_data\\项目实战\\尚硅谷大数据技术之电信客服综合案例\\2.资料\\2.资料\\辅助文档\\contact.log"));
        producer.setOut(new LocalFileDataOut("E:\\big_data\\项目实战\\尚硅谷大数据技术之电信客服综合案例\\2.资料\\2.资料\\辅助文档\\call_c.log"));
*/

        producer.setIn(new LocalFileDataIn(args[0]));
        producer.setOut(new LocalFileDataOut(args[1]));

        //生产数据
        producer.produce();

        //关闭生产者对象
        producer.close();

    }
}

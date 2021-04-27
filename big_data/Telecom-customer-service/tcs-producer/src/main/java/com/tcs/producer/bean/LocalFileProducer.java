package com.tcs.producer.bean;

/**
 * 本地数据文件的生产者
 */

import com.tcs.common.bean.DataIn;
import com.tcs.common.bean.DataOut;
import com.tcs.common.bean.Producer;
import com.tcs.common.util.DateUtil;
import com.tcs.common.util.NumberUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class LocalFileProducer implements Producer{

    private DataIn in;
    private DataOut out;
    private volatile boolean flg=true;

    public void setIn(DataIn in) {
        this.in=in;
    }

    public void setOut(DataOut out) {
        this.out=out;
    }

    //生产数据
    public void produce() {

        //读取通讯录数据
        try {
            List<Contact> contacts = in.read(Contact.class);

            while(flg){

                //从通讯录随机查找两个电话号码(主叫，被叫）
                int call1Index = new Random().nextInt(contacts.size());
                int call2Index;
                while(true){
                    call2Index = new Random().nextInt(contacts.size());
                    if(call1Index!=call2Index){
                        break;
                    }
                }

                Contact call1=contacts.get(call1Index);
                Contact call2=contacts.get(call2Index);

                //生成随机的通话时间
                String startDate="20180101000000";
                String endDate="20190101000000";

                long startTime= DateUtil.parse(startDate,"yyyyMMddHHmmss").getTime();
                long endTime= DateUtil.parse(endDate,"yyyyMMddHHmmss").getTime();

                //通话时间
                long callTime=startTime+(long)((endTime-startTime)*Math.random());
                //通话时间字符串
                String callTimeString=DateUtil.format(new Date(callTime),"yyyyMMddHHmmss");

                //生成随机的通话时长
                String duration = NumberUtil.format(new Random().nextInt(3000),4);

                //生成通话记录
                calllog log = new calllog(call1.getTel(),call2.getTel(),callTimeString,duration);

                System.out.println(log);
                //将通话记录刷写到数据文件中
                out.write(log);

                Thread.sleep(500);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关闭生产者
    public void close() throws IOException {
        if(in!=null) {
            in.close();
        }
        if(out!=null){
            out.close();
        }
    }
}

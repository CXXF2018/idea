package com.tcs.consumer.dao;

import com.tcs.common.bean.BaseDao;
import com.tcs.common.constant.Names;
import com.tcs.common.constant.ValConstant;
import com.tcs.consumer.bean.Calllog;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

//HBase访问对象
public class HbaseDao extends BaseDao {

    //初始化
    public void init() throws IOException {
        //为线程准备好connection对象和admin对象
        start();

        createNamespace(Names.NAMESPACE.getvalue());
        try {
            createTableXX(Names.TABLENAME.getvalue(), "tcs.ct.consumer.coprocesspr.InsertCalleeCoprocessor", ValConstant.REGION_COUNT,Names.CF_CALLER
                    .getvalue(),Names.CF_CALLEE.getvalue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        end();
    }

    //插入对象
    public void insertData(Calllog log) throws IOException, IllegalAccessException {
            log.setRowkey(genRegionNum(log.getCall1(),log.getCalltime())+"_"+log.getCall1()+"_"+log.getCalltime()
                    +"_"+log.getCall2()+"_"+log.getDuration()+"_"+log.getFlag());

            putData(log);
    }



    //插入数据
    public void insertData(String value) throws IOException {
        //将通话日志保存在HBase的表中

        //1.获取通话日志数据
        String[] values = value.split("\t");
        String call1=values[0];
        String call2=values[1];
        String calltime=values[2];
        String duration=values[3];

        //2.创建数据对象

        //rowkey的设计（长度原则，唯一性，散列性）
        /*
        * 1）长度原则
        *   最大值64KB，推荐长度10-100byte
        *   最好是8的倍数，能短则短，太长会影响性能
        * 2）唯一原则
        * 3）散列原理
        *   3-1）盐值散列 ：不能直接使用时间戳作为rowkey，要在前面加随机数
        *   3-2）字符串反转
        *   3-3）计算分区号
        *   */

        //rowkey=regionNum+call1+time+call2+duration;
        String rowkey=genRegionNum(call1,calltime)
                +"_"+call1+"_"+calltime+"_"+call2+"_"+duration+"_1";

        //主叫用户
        Put put = new Put(Bytes.toBytes(rowkey));

        put.addColumn(Bytes.toBytes(Names.CF_CALLER.getvalue()),Bytes.toBytes("call1"),Bytes.toBytes(call1));
        put.addColumn(Bytes.toBytes(Names.CF_CALLER.getvalue()),Bytes.toBytes("call2"),Bytes.toBytes(call2));
        put.addColumn(Bytes.toBytes(Names.CF_CALLER.getvalue()),Bytes.toBytes("calltime"),Bytes.toBytes(calltime));
        put.addColumn(Bytes.toBytes(Names.CF_CALLER.getvalue()),Bytes.toBytes("duration"),Bytes.toBytes(duration));
        put.addColumn(Bytes.toBytes(Names.CF_CALLER.getvalue()),Bytes.toBytes("flag"),Bytes.toBytes
                ("1"));

        String calleeRowkey=genRegionNum(call2,calltime)
                +"_"+call2+"_"+calltime+"_"+call1+"_"+duration+"_0";

        //3.保存数据
       // List<Put> puts=new ArrayList<Put>();
        //puts.add(put);
        //puts.add(calleeput);
        putData(Names.TABLENAME.getvalue(),put);
    }
}

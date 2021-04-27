package com.tcs.common.bean;

import com.tcs.common.api.Column;
import com.tcs.common.api.RowKey;
import com.tcs.common.api.TableRef;
import com.tcs.common.constant.Names;
import com.tcs.common.constant.ValConstant;
import com.tcs.common.util.DateUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//基础数据访问对象
public abstract class BaseDao {

    //threadlocal而是一个线程内部的存储类，可以在指定线程内存储数据
    // 数据存储以后，只有指定线程可以得到存储数据
    // 保证在这个线程中，我们所使用的数据库连接对象Connection都是同一个
    private ThreadLocal<Connection> connHolder= new ThreadLocal<Connection>();
    private ThreadLocal<Admin> adminHolder = new ThreadLocal<Admin>();

    protected void start() throws IOException {
        getConnection();
        getAdmin();
    }

    protected void end() throws IOException {
        Admin admin = getAdmin();
        if (admin != null) {
            admin.close();
            adminHolder.remove();
        }

        Connection connection = getConnection();
        if (connection != null) {
            connection.close();
            connHolder.remove();
        }
    }

    //获取连接对象
    protected synchronized Connection getConnection() throws IOException {
        Connection connection = connHolder.get();
        if (connection == null) {
            Configuration conf = HBaseConfiguration.create();
            connection = ConnectionFactory.createConnection(conf);
            connHolder.set(connection);
        }

        return connection;
    }

    //获取管理员对象
    protected synchronized Admin getAdmin() throws IOException {
        Admin admin = adminHolder.get();
        if (admin == null) {
            admin = getConnection().getAdmin();
            adminHolder.set(admin);
        }

        return admin;
    }



    //创建命名空间，若已经存在则不需要创建，否则创建新的
    protected void createNamespace(String namespace) throws IOException {
        Admin admin = getAdmin();

        try {
            NamespaceDescriptor namespaceDescriptor = admin.getNamespaceDescriptor(namespace);
        } catch (NamespaceNotFoundException e) {

            NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();
            admin.createNamespace(namespaceDescriptor);
        }
    }

    //创建表,如果表已经存在，那么就删除
    protected void createTableXX(String tableName,String copressorClass,Integer regionCount, String... cfs) throws Exception {
        //创建表
        createTable(tableName,regionCount,null,cfs);
    }

    //创建表,如果表已经存在，那么就删除
    protected void createTableXX(String tableName, String... cfs) throws Exception {
        createTable(tableName,null,null,cfs);
    }

    //创建表
    private void createTable(String tableName,Integer regionCount,String copprocessorClass,
                             String...
            cfs)
            throws
            Exception {
        Admin admin = getAdmin();

        dropTable(tableName);

        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));

        if(cfs==null||cfs.length<=0){
            cfs = new String[1];
            cfs[0]= Names.CF_INFO.getvalue();
        }
        for (String cf : cfs) {

            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
            hTableDescriptor.addFamily(hColumnDescriptor);
        }

        if(copprocessorClass!=null&&!copprocessorClass.equals("")){
            hTableDescriptor.addCoprocessor(copprocessorClass);
        }

        //增加预分区
        if(regionCount==null||regionCount<=0)
        {
            admin.createTable(hTableDescriptor);
        }
        else {
            byte[][] splitKeys=genSplitKeys(regionCount);
            admin.createTable(hTableDescriptor,splitKeys);
        }
    }

    //查询startrow，stoprow集合
    protected List<String[]> getStartStopRowKeys(String tel,String startTime,
                                                 String stopTime){

        List<String[]> rowKeyss=new ArrayList<String[]>();

        String beginTime=startTime.substring(0,6);
        String endTime=stopTime.substring(0,6);

        Calendar startCal=Calendar.getInstance();
        startCal.setTime(DateUtil.parse(beginTime,"yyyyMM"));

        Calendar stopCal=Calendar.getInstance();
        stopCal.setTime(DateUtil.parse(endTime,"yyyyMM"));

        while(startCal.getTimeInMillis()<stopCal.getTimeInMillis()){

            String currentTime=DateUtil.format(startCal.getTime(),"yyyyMM");

            int regionNum=genRegionNum(tel,currentTime);

            String startKey=regionNum+"_"+tel+"_"+currentTime;
            String stopKey=startKey+"|";

            String[] rowKeys={startKey,stopKey};
            rowKeyss.add(rowKeys);

            startCal.add(Calendar.MONTH,1);
        }

        return rowKeyss;
    }

    //分区号
    protected int genRegionNum(String tel, String date){

        //18810253537
        String usercode=tel.substring(tel.length()-4);//截取无规律部份

        //202008221233
        String yearMonth=date.substring(0,6);//截取年月

        int userCodeHash = usercode.hashCode();
        int yearMonthHash=yearMonth.hashCode();

        //crc校验采用的是异或算法
        int crc=Math.abs(userCodeHash^yearMonthHash);

        //取模

        int regionNum=crc% ValConstant.REGION_COUNT;

        return regionNum;

    }

    //生成分区键
    private byte[][]genSplitKeys(int regionCount){
        int splitKeyNum=regionCount-1;
        byte[][] bs = new byte[splitKeyNum][];

        ArrayList<byte[]> bsList = new ArrayList<byte[]>();
        for (int i = 0; i < splitKeyNum; i++) {

            String splitKey=i+"|";
            bsList.add(Bytes.toBytes(splitKey));

        }

        //Collections.sort(bsList,new Bytes.ByteArrayComparator());

        bsList.toArray(bs);
        return bs;
    }

    //增加对象
    protected void putData(Object obj) throws IOException, IllegalAccessException {

        //反射
        Class clazz = obj.getClass();
        TableRef tableRef = (TableRef) clazz.getAnnotation(TableRef.class);
        String tableName=tableRef.value();

        Field[] fs = clazz.getDeclaredFields();
        String stringRowKey="";
        for (Field f : fs) {
            RowKey rowKey = f.getAnnotation(RowKey.class);
            if(rowKey!=null){
                //在Java中可以通过反射进行获取实体类中的字段值，当未设置Field的setAccessible方法为true时，
                // 会在调用的时候进行访问安全检查，会抛出IllegalAccessException异常
                f.setAccessible(true);
                stringRowKey = (String)f.get(obj);
                break;
            }
        }

        //获取连接对象
        Connection connection = getConnection();

        //创建put对象
        Put put = new Put(Bytes.toBytes(stringRowKey));

        //获取表格对象
        Table table = connection.getTable(TableName.valueOf(tableName));

        //获取列信息并添加至put中
        for (Field f : fs) {
            Column column = f.getAnnotation(Column.class);
            if (column!=null){
                String family=column.family();
                String colName=column.column();
                if(colName==null||"".equals(colName)){
                    colName=f.getName();
                }
                f.setAccessible(true);
                String value=(String)f.get(obj);

                put.addColumn(Bytes.toBytes(family),Bytes.toBytes(colName),
                        Bytes.toBytes(value));
            }
        }

        //增加数据
        table.put(put);

        //关闭表
        table.close();
    }


    //增加数据
    protected void putData(String tableName, Put put) throws IOException {
        //获取表对象
        Connection connection = getConnection();
        Table table = connection.getTable(TableName.valueOf(tableName));

        //增加数据
        table.put(put);

        //关闭资源
        table.close();

    }

    //增加多条数据
    protected void putData(String tableName, List<Put> puts) throws IOException {
        //获取表对象
        Connection connection = getConnection();
        Table table = connection.getTable(TableName.valueOf(tableName));

        //增加数据
        table.put(puts);

        //关闭资源
        table.close();

    }

    //删除表
    protected void dropTable(String tableName) throws IOException {
        Admin admin = getAdmin();
        if (admin.tableExists(TableName.valueOf(tableName))) {
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));
        }
    }



}

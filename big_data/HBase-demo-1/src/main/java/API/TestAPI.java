package API;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class TestAPI {

    private  static  Admin admin =null;
    private  static Connection connection = null;

    static {

        try {
            //1.获取配置信息
            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum","hadoop01,hadoop02,hadoop03");

            //2.创建connection对象
            connection = ConnectionFactory.createConnection(configuration);

            //3.获取admin对象
            admin = connection.getAdmin();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //1.判断表是否存在
    public static boolean isTableExit(String tableName) throws IOException {


        //4.判断表是否存在
        boolean exits = admin.tableExists(TableName.valueOf(tableName));

        //6.返回结果
        return exits;
    }

    //2.创建表
    public static void createTale(String tableName,String... cfs) throws IOException {

        //4.判断是否存在列族信息
        if(cfs.length<=0){
            System.out.println("请设置列族信息");
            return;
        }

        //5.判断表是否存在
        if(admin.tableExists(TableName.valueOf(tableName))){
            System.out.println("表已经存在");
            return;
        }

        //6.创建表描述器
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));

        //7.添加列族信息
        for (String cf : cfs) {

            //8.创建列族描述器
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);

            //9.添加列族信息
            tableDescriptor.addFamily(hColumnDescriptor);
        }

        //10.创建表
        admin.createTable(tableDescriptor);

    }


    public static void close(){

        if(admin!=null){
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //关闭资源
        if(connection!=null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //3.删除表
    public static void dropTable(String tableName) throws IOException {

        if(!isTableExit(tableName)){
            System.out.println(tableName+"表不存在");
        }else{
            //使表下线
            admin.disableTable(TableName.valueOf(tableName));

            //3.删除表
            admin.deleteTable(TableName.valueOf(tableName));
        }
    }

    //4.创建命名空间
    public static void createNamespace(String ns){

        //创建命名空间描述器
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(ns).build();

        //创建命名空间
        try {
            admin.createNamespace(namespaceDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //5.插入数据
    public static void putData(String tableName,String rowkey,String cf,String cn,String value) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));

        Put put = new Put(Bytes.toBytes(rowkey));

        put.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn),Bytes.toBytes(value));

        table.put(put);

        table.close();
    }

    //6.获取数据
    public static void getData(String tableName,String rowkey,String cf,String cn) throws IOException {

        //1.获取表对象
        Table table = connection.getTable(TableName.valueOf(tableName));

        //2.创建get对象
        Get get = new Get(Bytes.toBytes(rowkey));

        //2.1指定获取的列族
        get.addFamily(Bytes.toBytes(cf));

        //3.获取数据
        Result result = table.get(get);

        //4.解析数据并打印
        for (Cell cell : result.rawCells()) {

            System.out.println(Bytes.toBytes("value:" + CellUtil.cloneValue(cell)));
        }

        //关闭表连接
        table.close();
    }

    //7.获取数据（scan）
    public static void scanTable(String tableName) throws IOException {

        //创建表对象
        Table table = connection.getTable(TableName.valueOf(tableName));

        //创建scan对象
        Scan scan = new Scan();//此处可以设置扫描条件

        //扫描表
        ResultScanner resultScanner = table.getScanner(scan);

        //解析result
        for (Result result : resultScanner) {

            //解析result并打印
            for (Cell cell : result.rawCells()) {

                System.out.println(Bytes.toBytes("value:" + CellUtil.cloneValue(cell)));
            }
        }

        table.close();
    }

    //8.删除数据
    public static void deleteData(String tableName,String rowkey,String cf,String cn) throws IOException {

        //获取表对象
        Table table = connection.getTable(TableName.valueOf(tableName));

        //创建删除对象
        Delete delete = new Delete(Bytes.toBytes(rowkey));

        //设置删除的列
        delete.addColumns(Bytes.toBytes(cf),Bytes.toBytes(cn));//删除指定列的所有版本
        delete.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn));//删除指定列最新的版本

        //执行删除操作
        table.delete(delete);

        //关闭资源
        table.close();
    }


    public static void main(String[] args) throws IOException {

        //1.判断表是否存在
        System.out.println(isTableExit("fruit1"));

        //2.创建表
        createTale("create_table_test","info","info2");

       /* //3.删除表
        dropTable("stu");

        //4.创建命名空间测试
        createNamespace("0408");

        //5.插入数据
        putData("0408:stu" ,"1001","info","name","dada");

        //6.获取单行数据
        getData("0408:stu","1001","info","name");

        //7.scan
        scanTable("0408:stu");

        //8.删除数据
        deleteData("0408:stu","1001","info","name");*/

        //关闭资源
        close();
    }
}

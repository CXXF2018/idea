import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseAPI {

    public static Connection connection=null;
    public static Admin admin=null;


    static{

        try {
            //获取配置文件
            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum","hadoop01,hadoop02,hadoop03");

            //创建connection连接对象
            connection = ConnectionFactory.createConnection(configuration);

            //获取admin对象
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //1.判断表是否存在
    public static boolean isTableExit(String tableName) throws IOException {
        boolean exists = admin.tableExists(TableName.valueOf(tableName));

        return exists;
    }

    //2.创建表
    public static void createTable(String tableName,String...cfs) throws IOException {
        //判断是否存在列族信息
        if (cfs.length==0){
            System.out.println("请设置列族信息");
            return;
        }

        //判断表是否存在
        boolean tableExit = admin.tableExists(TableName.valueOf(tableName));
        if (tableExit){
            System.out.println("表已经存在");
            return;
        }

        //创建表描述器
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));

        //添加列族信息
        for (String cf : cfs) {

            //创建列族描述器
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);

            //添加列族信息
            tableDescriptor.addFamily(hColumnDescriptor);
        }

        //创建表
        admin.createTable(tableDescriptor);

        System.out.println("表创建成功");
    }

    //3.删除表
    public static void dropTable(String tableName) throws IOException {
        if (!isTableExit(tableName)){
            System.out.println(tableName + "表不存在");
        }else {
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));
            System.out.println("表删除成功");
        }
    }

    //4.向表中插入数据
    public static void putData(String tableName,String rowKey,String cf,String cn,String value) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn),Bytes.toBytes(value));
        table.put(put);
        table.close();
    }

    //5.删除多行数据
    public static void deleteData(String tableName,String rowkey, String cf, String cn) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));

        Delete delete = new Delete(Bytes.toBytes(rowkey));

        delete.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn));

        table.delete(delete);
        System.out.println("删除成功");

        table.close();

    }

    //6.获取所有数据
    public static void scanTable(String tableName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));

        Scan scan = new Scan();
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            for (Cell cell : result.rawCells()) {
                System.out.println(Bytes.toString(CellUtil.cloneRow(cell)) + ":" + Bytes.toString(CellUtil.cloneValue
                        (cell)));
            }
        }
        table.close();
    }

    //7.获取某一行数据
    public static void getData(String tableName, String rowkey, String cf, String cn) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowkey));
        get.addFamily(Bytes.toBytes(cf));
        Result result = table.get(get);
        for (Cell cell : result.rawCells()) {
            System.out.println(Bytes.toString(CellUtil.cloneRow(cell))+":"+Bytes.toString(CellUtil
                    .cloneValue(cell)));
            System.out.println();
        }
        table.close();
    }

    //9.创建命名空间
    public static void createNameSpace(String ns){
        //创建命名空间描述器
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(ns).build();

        //创建命名空间
        try {
            admin.createNamespace(namespaceDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //10.关闭资源
    public static void close() {

        if (admin != null) {
            try {
                admin.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        //关闭资源
        if (connection != null) {
            try {
                connection.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {

        //1.判断表是否存在
        System.out.println(isTableExit("fruit"));

        //2.创建表
        //createTable("test_20201005","info");

        //3.删除表
        //dropTable("fruit");

        //4.创建命名空间
        //createNameSpace("20201005");

        //5.向表中插入数据
        //putData("fruit","1002","info","name","apple");

        //6.获取数据
        //getData("fruit","1001","info","name");

        //7.获取所有数据
        //scanTable("fruit");

        //8.删除数据
        deleteData("fruit","1001","info","name");

        //关闭资源
        close();


    }
}

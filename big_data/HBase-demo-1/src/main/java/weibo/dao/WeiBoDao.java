package weibo.dao;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import weibo.constant.Names;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeiBoDao {

    //获取配置信息，创建连接
    public static Connection connection=null;
    public static Admin admin=null;
    static {
        try {
            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum","hadoop01,hadoop02,hadoop03");
            connection = ConnectionFactory.createConnection(configuration);

            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //关闭资源
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


    //创建命名空间
    public static void createNameSpace(String ns) throws IOException {

        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(ns).build();
        admin.createNamespace(namespaceDescriptor);
    }

    //创建表格
    public static void createTable(String tableName,Integer versions,String...cfs) throws IOException {

        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName.valueOf(tableName));

        for (String cf : cfs) {
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
            hColumnDescriptor.setMaxVersions(versions);
            hTableDescriptor.addFamily(hColumnDescriptor);
        }
        admin.createTable(hTableDescriptor);
    }

    //创建表格重载
    public static void createTable(String tableName,String...cfs) throws IOException {
        createTable(tableName,1,cfs);
    }

    //想指定列插入一行数据
    public static void putCell(String tableName,String rowKey,String family,String column,String
            value) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(family),Bytes.toBytes(column),Bytes.toBytes(value));
        table.put(put);
        table.close();
    }

    //将向不同的roekey对应的列中插入相同的一条数据
    public static void putCells(String tableName,List<String> rowKeys,String family,String
            column,String
            value) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        ArrayList<Put> puts = new ArrayList<Put>();

        for (String rowKey : rowKeys) {
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(family),Bytes.toBytes(column),Bytes.toBytes(value));
            puts.add(put);
        }
        table.put(puts);
        table.close();
    }

    //通过rowkey删除一行数据
    public static void deleteRow(String tableName,String rowKey) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        table.delete(delete);
        table.close();
    }

    //删除表中rowkey对应的某一数据的所有版本  addColumns而不是addColumn
    public  static void deleteCells(String tableName,String rowKey,String family,String column) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumns(Bytes.toBytes(family),Bytes.toBytes(column));
        table.delete(delete);

        table.close();
    }

    //根据rowkey前缀获取相关信息
    public List<String> getRowKeysByPrefix(String tableName, String prefix) throws IOException {
        ArrayList<String> list = new ArrayList<String>();

        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setRowPrefixFilter(Bytes.toBytes(prefix));
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            String rowkey = result.getRow().toString();
            list.add(rowkey);
        }

        scanner.close();
        table.close();

        return list;
    }

    public List<String> getCellsByPrefix(String tableName,String prefix,String family,String
            column) throws IOException {

        ArrayList<String> list = new ArrayList<String>();

        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setRowPrefixFilter(Bytes.toBytes(prefix));
        scan.addColumn(Bytes.toBytes(family),Bytes.toBytes(column));
        ResultScanner scanner = table.getScanner(scan);

        for (Result result : scanner) {
            Cell[] cells = result.rawCells();
            byte[] value = CellUtil.cloneValue(cells[0]);//此处不明白为什么是【0】，精确到了列为什么还是数组
            list.add(Bytes.toString(value));
        }
        table.close();
        scanner.close();

        return list;
    }

    public List<String> getFamilyByRowKey(String tableName,String rowKey,String family) throws
            IOException {

        ArrayList<String> list = new ArrayList<String>();
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        get.setMaxVersions(Names.INBOX_DATA_VERSIONS);
        get.addFamily(Bytes.toBytes(family));
        Result result = table.get(get);

        for (Cell cell : result.rawCells()) {
            list.add(Bytes.toString(CellUtil.cloneValue(cell)));
        }
        table.close();

        return list;
    }

    public List<String> getCellsByRowKey(String tableName,List<String> rowKeys,String family,
                                         String column) throws IOException {
        ArrayList<String> weibos = new ArrayList<String>();

        Table table = connection.getTable(TableName.valueOf(tableName));

        ArrayList<Get> gets = new ArrayList<Get>();

        for (String rowKey : rowKeys) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(family),Bytes.toBytes(column));
            gets.add(get);
        }

        Result[] results = table.get(gets);
        for (Result result : results) {
            Cell cell = result.rawCells()[0];//0?????
            String weibo = Bytes.toString(CellUtil.cloneValue(cell));
            weibos.add(weibo);
        }
        table.close();

        return weibos;
    }

    public List<String> getRowKeysByRange(String tableName, String startRow, String stopRow) throws IOException {
        ArrayList<String> list = new ArrayList<String>();
        Table table = connection.getTable(TableName.valueOf(tableName));

        Scan scan = new Scan(Bytes.toBytes(startRow),Bytes.toBytes(stopRow));

        ResultScanner scanner = table.getScanner(scan);

        for (Result result : scanner) {
            byte[] row = result.getRow();
            list.add(Bytes.toString(row));

        }

        return list;
    }
}

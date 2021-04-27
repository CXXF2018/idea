package perparedStatement;

import Bean.Customers;
import Util.JDBCUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

//针对不同表的通用查询操作
public class preParedStatementQueryTest {

    @Test
    public void testGetInstance(){
        String sql="select * from customer where id=?";
        Customers customers = getInstance(Customers.class, sql, 12);
    }

    //处理单条数据的场合
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //获取连接对象
            connection = JDBCUtil.getConnection();

            //预编译sql语句
            preparedStatement = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            //执行sql语句，返回结果集
            resultSet = preparedStatement.executeQuery();

            //获取元数据信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            if (resultSet.next()) {

                T t = clazz.newInstance();//此处为通用性的重点

                for (int i = 0; i < columnCount; i++) {

                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取列名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    //给指定对象指定属性
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCUtil.closeResurce(connection, preparedStatement, resultSet);
        }

        return null;
    }

    @Test
    public void testGetForList(){
        String sql="select * from customer where id=?";
        List<Customers> list = getForList(Customers.class, sql, 12);
        list.forEach(System.out::println);
    }

    //处理多条数据的场合
    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //获取连接对象
            connection = JDBCUtil.getConnection();

            //预编译sql语句
            preparedStatement = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            //执行sql语句，返回结果集
            resultSet = preparedStatement.executeQuery();

            //获取元数据信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            ArrayList<T> list = new ArrayList<>();
            while(resultSet.next()) {

                T t = clazz.newInstance();//此处为通用性的重点

                for (int i = 0; i < columnCount; i++) {

                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取列名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    //给指定对象指定属性
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }

                list.add(t);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCUtil.closeResurce(connection, preparedStatement, resultSet);
        }

        return null;
    }

}

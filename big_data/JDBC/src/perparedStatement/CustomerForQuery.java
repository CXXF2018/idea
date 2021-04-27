package perparedStatement;

import Bean.Customers;
import Util.JDBCUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

//针对customer表的查询操作
public class CustomerForQuery {

    @Test
    public void testQuery(){

        Connection connection= null;
        PreparedStatement preparedStatement= null;
        ResultSet resultSet = null;
        try {
            connection = null;
            preparedStatement = null;
            //1.创建连接
            connection = JDBCUtil.getConnection();

            //2.预编译sql语句
            String sql="select * from customers where name=?";
            preparedStatement = connection.prepareStatement(sql);

            //3.填充占位符
            preparedStatement.setObject(1,"fsdf");

            //4.执行语句，返回结果
            resultSet = preparedStatement.executeQuery();

            //5.对结果进行解析
            if(resultSet.next()){//此处next（）的作用类似于之前所学习的hasnext，但是它会自动指向下一条数据
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //将数据封装为一个对象
                Customers customers = new Customers(id, name, email, birth);
                System.out.println(customers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //6.关闭资源
            JDBCUtil.closeResurce(connection,preparedStatement,resultSet);
        }



    }

    @Test
    public void testQueryForCommon(){
        String sql="select * from customers where name=?";
        Customers customer = QueryForCommon(sql, "dsd");
        System.out.println(customer);
    }
    public Customers QueryForCommon(String sql,Object... args){

        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet = null;

        try {
            //获取连接对象
            connection = JDBCUtil.getConnection();

            //预编译sql语句
            preparedStatement = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {

                preparedStatement.setObject(i+1,args[i]);

            }

            //获取结果
            resultSet = preparedStatement.executeQuery();

            //获取输出结果的字段数，该方法在结果集的元数据中
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            if(resultSet.next()){
                Customers customer = new Customers();

                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取每列的列名
                    String columnName = metaData.getColumnName(i + 1);

                    //给customers中的columnName属性赋值为columnValue,可以通过反射拿到对应的属性
                    Field declaredField = Customers.class.getDeclaredField(columnName);
                    declaredField.setAccessible(true);//防止属性是私有的
                    declaredField.set(customer,columnValue);

                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCUtil.closeResurce(connection,preparedStatement,resultSet);

            return null;
        }

    }
}



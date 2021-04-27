package dao;

import JDBCUtil.JDBCUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//封装了针对数据的通用操作
public abstract class BaseDAO<T> {
    private Class<T> clazz=null;

    {
        //获取当前basedao的子类继承的父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//获取父类的泛型
        clazz= (Class<T>) actualTypeArguments[0];
    }

    //通用的增删改
    public int Update(Connection connection,String sql,Object...args) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = null;

            //2.预编译sql语句
            preparedStatement = connection.prepareStatement(sql);

            //3.填充占位符
            for (int i = 0; i <args.length ; i++) {
                preparedStatement.setObject(i+1,args[i]);
            }

            //4.执行语句
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            connection.setAutoCommit(true);
            /*若此时 Connection 没有被关闭，还可能被重复使用，则需要恢复其自动提交状态 setAutoCommit(true)。
            尤其是在使用数据库连接池技术时，执行close()方法前，建议恢复自动提交状态。*/

            //5.关闭资源
            JDBCUtil.closeResurce(null,preparedStatement);
        }

        return 0;
    }

    //通用的查询
    public T getInstance(Connection connection, String sql, Object... args) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
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
            JDBCUtil.closeResurce(null, preparedStatement, resultSet);
        }

        return null;
    }

    //处理多条数据的场合
    public List<T> getForList(Connection connection,String sql, Object...
            args) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
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
            JDBCUtil.closeResurce(null, preparedStatement, resultSet);
        }

        return null;
    }

    //查询特殊值的通用方法
    public <E> E getValue(Connection connection,String sql,Object... args){

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }

            rs = ps.executeQuery();
            if (rs.next()){
                return (E)rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResurce(null,ps,rs);
        }

        return null;
    }
}

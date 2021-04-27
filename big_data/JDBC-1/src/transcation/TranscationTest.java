package transcation;

import JDBCUtil.JDBCUtil;
import bean.User;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

//事务的处理

/*当一个连接对象被创建时，默认情况下是自动提交事务：
        每次执行一个 SQL 语句时，如果执行成功，就会 向数据库自动提交，而不能回滚。
        关闭数据库连接，数据就会自动的提交。
        如果多个操作，每个操作使用的是自己单独的连接，则无法保证事务。
        即同一个事务的多个操作必须在同一个连接下。

        调用 Connection 对象的 setAutoCommit(false); 以取消自动提交事务
        在所有的 SQL 语句都成功执行后，调用 commit();
        方法提交事务 在出现异常时，调用 rollback(); 方法回滚事务*/

public class TranscationTest {

    public void testUpdateWithTx(){
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection();

            connection.setAutoCommit(false);

            String sql1="update user_table set balance =balance -100 where user=?";
            Update(connection,sql1,"AA");

            //模拟网络异常（异常会导致回滚）
            //System.out.println(10 / 0);

            String sql2="update user_table set balance =balance +100 where user=?";
            Update(connection,sql2,"BB");

            System.out.println("转账成功");

            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            JDBCUtil.closeResurce(connection,null);
        }

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

    /***********************************************************************************************/

    @Test
    public void testTransactionSelect() throws Exception {
        Connection connection = JDBCUtil.getConnection();

        //设置隔离级别
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        connection.setAutoCommit(false);

        String sql="select * from user_table where user=?";
        User user = getInstance(connection, User.class, sql, "BB");

        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection connection = JDBCUtil.getConnection();

        //查看隔离级别
        System.out.println(connection.getTransactionIsolation());

        //设置隔离级别
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        connection.setAutoCommit(false);
        String sql="update user_table set balance=? where user=?";
        Update(connection,sql, 40,"BB");

        Thread.sleep(1500);
        System.out.println("修改结束");

    }

    public <T> T getInstance(Connection connection,Class<T> clazz, String sql, Object... args) {

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
}

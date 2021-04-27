package perparedStatement;

//使用PreparedStatement代替statement进行数据的增删查改

import Util.JDBCUtil;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class preparedStatementUpdateTest {

    //插入测试
    @Test
    public void InsertTest(){

        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            //1.获取配置文件中的信息
            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties properties = new Properties();
            properties.load(resourceAsStream);

            String url=properties.getProperty("url");
            String user=properties.getProperty("user");
            String password=properties.getProperty("password");
            String driverClass=properties.getProperty("driverClass");

            //2.加载驱动
            Class.forName(driverClass);

            //3.获取连接
            connection = DriverManager.getConnection(url, user, password);

            //4.预编译sql语句，返回preparedStatement的实例
            String sql="insert into customers(name,email,birth) VALUES(?,?,?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,"哪吒");
            preparedStatement.setString(2,"3412@gmail.com");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = simpleDateFormat.parse("2020-01-05");
            preparedStatement.setDate(3,new Date(date.getTime()));

            //5.执行程序
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //6.关闭资源
            try {
                if(preparedStatement!=null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(connection!=null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



    }

    //修改consumer表的一条记录
    @Test
    public void Update() {

        Connection connection=null;
        PreparedStatement preparedStatement =null;

        try {
            //1.获取数据库连接
            connection = JDBCUtil.getConnection();
            //2.预编译sql语句
            String sql="update customers set name=? where id=?";
            preparedStatement = connection.prepareStatement(sql);

            //3.填充占位符
            preparedStatement.setObject(1,"xiaofei");
            preparedStatement.setObject(2,18);

            //4.执行程序
            preparedStatement.execute();
            //该方法有返回值，是查询操作则返回true，是增删改则返回false
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //5.关闭资源
            JDBCUtil.closeResurce(connection, preparedStatement);
        }
    }

    //通用的增删改


    @Test
    public void testCommonUpdate() throws Exception {
       /* String sql="delete from cumstomers where id=?";
        testUpdate(sql,3);*/

       String sql="update 'order' set order_name=? where order_id=?";
       testUpdate(sql,"dd",2);


    }

    public void testUpdate(String sql,Object...args) throws Exception {

        Connection connection =null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = null;
            //1.获取连接
            connection =JDBCUtil.getConnection();

            //2.预编译sql语句
            preparedStatement = connection.prepareStatement(sql);

            //3.填充占位符
            for (int i = 0; i <args.length ; i++) {
                preparedStatement.setObject(i+1,args[i]);
            }

            //4.执行语句
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.关闭资源
            JDBCUtil.closeResurce(connection,preparedStatement);
        }


    }

}

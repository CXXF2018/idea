package Util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//操作数据库的工具类
public class JDBCUtil {

    //获取数据库连接
        public static Connection getConnection() throws Exception {
            Connection connection=null;
            PreparedStatement preparedStatement=null;
            //1.获取配置文件中的信息
            //ClassLoader根据一个指定的类的全限定名,找到对应的Class字节码文件,然后加载它转化成一个java.lang.Class类的一个实例.

            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

             Properties properties = new Properties();
             properties.load(resourceAsStream);

             String url=properties.getProperty("url");
             String user=properties.getProperty("user");
             String password=properties.getProperty("password");
             String driverClass=properties.getProperty("driverClass");

            //2.加载驱动
//            Class.forName：返回与给定的字符串名称相关联类或接口的Class对象
            Class.forName(driverClass);

            //3.获取连接
            connection = DriverManager.getConnection(url, user, password);

            return connection;
        }

        //关闭资源
        public static void closeResurce(Connection connection,PreparedStatement preparedStatement){
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

    public static void closeResurce(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
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

            try {
                if(resultSet!=null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    /*获得ClassLoader的几种方法可以通过如下3种方法得到ClassLoader
            this.getClass().getClassLoader(); //使用当前类的ClassLoader
            Thread.currentThread().getContextClassLoader(); //使用当前线程的ClassLoader
            ClassLoader.getSystemClassLoader(); //使用系统ClassLoader,即系统的入口点所使用的ClassLoader。
           (注意, system ClassLoader与根ClassLoader并不一样。JVM下systemClassLoader通常为App ClassLoader)

    Class.getResourse()和Class.getClassLoader().getResource()
    这两个getResource()是使用当前ClassLoader加载资源(即资源在Class path中)，这样资源和
    class直接打在jar包中,避免文件路径问题.两者不同是Class的getResource()方法是从当.
    前.class文件路径查找资源,ClassLoader则是从jar包根目录查找.*/



}

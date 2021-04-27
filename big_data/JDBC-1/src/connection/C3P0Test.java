package connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.sql.Connection;

public class C3P0Test {
    @Test
    //不推荐这种方式
    public void testGetConneciton() throws Exception {
        //获取C3P0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("xdl");

        cpds.setInitialPoolSize(10);//设置初始时数据库连接池中的连接数

        Connection connection = cpds.getConnection();
        System.out.println(connection);

        //销毁C3P0连接池
//        DataSources.destroy(cpds);
    }

    @Test
    //使用配置文件
    public void testGetConnection1() throws Exception {

        ComboPooledDataSource cpds= new ComboPooledDataSource("helloc3p0");
        Connection connection = cpds.getConnection();
        System.out.println(connection);
    }
}

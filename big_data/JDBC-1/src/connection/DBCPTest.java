package connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBCPTest {

    @Test
    //方式一  硬编码，不推荐
    public void testGetConnection() throws SQLException {
        //创建数据库连接池
        BasicDataSource basicDataSource = new BasicDataSource();

        //设置基本信息
        basicDataSource.setUrl("jdbc:mysql:///test");
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("xdl");

        //设置其他的相关属性
        basicDataSource.setMaxActive(10);
        basicDataSource.setInitialSize(10);

        Connection connection = basicDataSource.getConnection();
        System.out.println(connection);
    }

    //方式二、使用配置文件
    private static DataSource dataSource;
    static {
        Properties properties = new Properties();

        //方式一
        //InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");

        try {
            //方式二
            FileInputStream fileInputStream = new FileInputStream(new File("src/dbcp.properties"));
            properties.load(fileInputStream);

            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
        public void testGetConnection1() throws Exception {

        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}

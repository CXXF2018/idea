package connection;


import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

public class DruidTest {

    private static DataSource dataSource;
    static {
        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(new File("src/dbcp.properties"));

            properties.load(fileInputStream);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getConnection() throws Exception {

        Connection connection = dataSource.getConnection();
        System.out.println(connection);

    }
}

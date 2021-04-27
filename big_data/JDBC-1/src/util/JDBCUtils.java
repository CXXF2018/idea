package util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCUtils {

    //c3p0的数据库连接池技术
    public static Connection getConnection1() throws SQLException {
        ComboPooledDataSource cpds= new ComboPooledDataSource("helloc3p0");
        Connection connection = cpds.getConnection();

        return connection;
    }
}

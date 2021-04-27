package ConnectionTest;

import JDBCUtil.JDBCUtil;
import org.junit.Test;

import java.sql.Connection;

public class testConnection {

    @Test
    public void TestConnection() throws Exception {
        Connection connection = JDBCUtil.getConnection();
        System.out.println(connection);;
    }
}

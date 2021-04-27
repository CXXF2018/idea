package dbUtils;

import JDBCUtil.JDBCUtil;
import bean.Customers;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class QueryRunnerTest {

    @Test
    public void testInsert(){
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtil.getConnection();
            String sql="insert into customers(name,email,brith)values(?,?,?)";
            int update = queryRunner.update(connection, sql, "dsa", "daasd", "1212");
            System.out.println(update);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResurce(connection,null);
        }
    }

    @Test
    public void testQuery(){
        Connection connection=null ;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtil.getConnection();
            String sql="select * from customers where id<?";
            BeanListHandler<Customers> customersBeanListHandler = new BeanListHandler<>(Customers.class);

            List<Customers> query = queryRunner.query(connection, sql, customersBeanListHandler, 23);
            query.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResurce(connection,null);
        }

    }
}

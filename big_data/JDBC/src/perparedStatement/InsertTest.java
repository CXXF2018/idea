package perparedStatement;

import Util.JDBCUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

//向goods中传递20000条数据
public class InsertTest {
    public void TestInsert(){
        Connection connection = null;
        PreparedStatement preparedStatement=null;
        String sql="insert into goods(name)VALUES (?)";

        try {
            connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < 20000; i++) {
                preparedStatement.setObject(1,"name_"+i);
                preparedStatement.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            JDBCUtil.closeResurce(connection,preparedStatement);
        }

    }

        //优化的方案
        public void TestInsert1() {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            String sql = "insert into goods(name)VALUES (?)";

            try {
                connection = JDBCUtil.getConnection();
                preparedStatement = connection.prepareStatement(sql);

                for (int i =0; i < 30000; i++) {
                    preparedStatement.setObject(1, "name_" + i);

                    preparedStatement.addBatch();
                    if (i % 500 == 0) {
                        preparedStatement.executeBatch();
                        preparedStatement.clearBatch();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                JDBCUtil.closeResurce(connection, preparedStatement);
            }

        }

        //终极优化方案:不允许自动提交数据
    @Test
        public void TestInsert2() {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            String sql = "insert into goods(name)VALUES (?)";

            try {
                connection = JDBCUtil.getConnection();

                //设置不允许自动提交数据
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement(sql);

                for (int i =0; i <=300000; i++) {
                    preparedStatement.setObject(1, "name_" + i);

                    preparedStatement.addBatch();
                    if (i % 500 == 0) {
                        preparedStatement.executeBatch();
                        preparedStatement.clearBatch();
                    }

                }
                connection.commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                JDBCUtil.closeResurce(connection, preparedStatement);
            }

        }

    }

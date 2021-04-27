package perparedStatement;

import Bean.Order;
import Util.JDBCUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class OrderForQuery {

    @Test
    public void testOrderForQuery(){
        String sql="select order_id orderId,roder_name,order_date from order where orderId=?";
        orderForQuery(sql);
    }

    public Order orderForQuery(String sql,String...args){

        Connection connection=null;
        PreparedStatement preparedStatement =null;
        ResultSet resultSet=null;

            try {
                //获取连接对象
                connection = JDBCUtil.getConnection();

                //预编译sql语句
                preparedStatement = connection.prepareStatement(sql);

                //填充占位符
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i + 1, args[i]);
                }

                //执行，获取结果集
                resultSet = preparedStatement.executeQuery();

                //获取元数据信息
                ResultSetMetaData metaData = resultSet.getMetaData();

                int columnCount = metaData.getColumnCount();

                if (resultSet.next()) {
                    Order order = new Order();

                    for (int i = 0; i < columnCount; i++) {
                        //获取列值
                        Object columnValue = resultSet.getObject(i + 1);

                        //获取列的别名     此处不用列名是因为若表中名称和API中的名称不一样会导致异常
                        String columnLabel = metaData.getColumnLabel(i + 1);

                        //通过反射进行赋值
                        Field field = Order.class.getDeclaredField(columnLabel);
                        field.setAccessible(true);
                        field.set(order, columnValue);
                    }
                    return order;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                //关闭资源
                JDBCUtil.closeResurce(connection,preparedStatement,resultSet);
            }

        return null;
    }
}

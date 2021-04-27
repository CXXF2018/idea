package perparedStatement;

import Bean.Customers;
import Util.JDBCUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class BlobTest {

    public void testInsert() throws Exception {

        //创建连接
        Connection connection = JDBCUtil.getConnection();
        String sql="insert into customers(name,email,brith,photo)VALUES (?,?,?,?)";

        //预编译sql语句
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //填充占位符
        preparedStatement.setString(1,"asddfs");
        preparedStatement.setString(2,"asda");
        preparedStatement.setDate(3,new Date(new java.util.Date().getTime()));

        FileInputStream fs = new FileInputStream("das.jpg");
        preparedStatement.setBlob(4,fs);

        preparedStatement.execute();

        fs.close();
        JDBCUtil.closeResurce(connection,preparedStatement);


    }

    public void testQuery() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //创建连接
            connection = JDBCUtil.getConnection();
            String sql="SELECT* from customers WHERE id=?";

            //预编译sql语句
            preparedStatement = connection.prepareStatement(sql);

            //填充占位符
            preparedStatement.setInt(1,4);

            //执行程序，返回结果
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date date = resultSet.getDate(4);

                Customers customers = new Customers(id, name, email, date);
                System.out.println(customers);

                //此处要会写
                Blob photo = resultSet.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("vsc");
                byte[] buffer=new byte[1024];
                int len;
                while((len=is.read(buffer))!=-1) {
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtil.closeResurce(connection,preparedStatement,resultSet);
        }
    }
}

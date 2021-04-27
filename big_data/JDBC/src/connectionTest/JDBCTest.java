package connectionTest;

import com.mysql.jdbc.Driver;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCTest {

    //方式一：driver
    public void TestConnection1() throws SQLException{

        Driver driver = new Driver();
        String url="jdbc:mysql://localhost:3306/test";//统一资源定位符
        //jdbc:mysql表示协议
        //localhost：ip地址
        //3306默认端口号
        //test  数据库
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","xdl");
        Connection connect = driver.connect(url, properties);

        System.out.println(connect);
    }

    //方式二：对方式一的迭代,没有出现第三方的API，使得程序有更好的移植性
    public void TestConnection2() throws Exception {

        //获取driver实现类对象，使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        String url="jdbc:mysql://localhost:3306/test";
        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","xdl");
        Connection connect = driver.connect(url, properties);

        System.out.println(connect);
    }

    //方式三：用DriverManager替换Driver，是一个具体的类
    public void TestConnection3() throws Exception {

        //获取driver实现类对象，使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        /*newInstance( )是一个方法，而new是一个关键字
        其次，Class下的newInstance()的使用有局限，因为它生成对象只能调用无参的构造函数
        Class.forName("")返回的是类
        Class.forName("").newInstance(返回的是object)
        从JVM的角度看，我们使用关键字new创建一个类的时候，这个类可以没有被加载。
        但是使用newInstance()方法的时候，就必须保证：1、这个 类已经加载；2、这个类已经连接了。
        而完成上面两个步骤的正是Class的静态方法forName()所完成的，这个静态方法调用了启动类加载器，
        即加载 java API的那个加载器。
*/


                        //注册启动
        DriverManager.registerDriver(driver);

        String url="jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="xdl";
        //获取连接
        //DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","xdl");
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);
    }

    //方式四：对三的优化
    public void TestConnection4() throws Exception {

        String url="jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="xdl";

        //加载driver，此处进行了优化，Driver中有一个静态代码块，随着类的加载执行
        Class.forName("com.mysql.jdbc.Driver");
       /* Driver driver = (Driver) clazz.newInstance();

        //注册启动
        DriverManager.registerDriver(driver);*/

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);
    }

    //方式五：通过配置文件获取基本的信息，实现了数据和代码的分离
    public void TestConnection5() throws Exception {

        //获取当前文件的配置信息
        InputStream resourceAsStream = JDBCTest.class.getClassLoader().getResourceAsStream("jdbc.properties");//由系统类加载器帮忙加载

        Properties properties = new Properties();
        properties.load(resourceAsStream);
        //加载driver，此处进行了优化，Driver中有一个静态代码块，随着类的加载执行
        Class.forName(properties.getProperty("driverClass"));

        String url=properties.getProperty("url");
        String user=properties.getProperty("user");
        String password=properties.getProperty("password");
        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);
    }

  /*  连接数据库的步骤：
    1）导入配置文件信息，当前类的系统类加载器帮忙加载，并转换成输入流
    2）将配置文件中的信息赋值给变量
    3）加载driver，此处使用的是Class.forName
    4）使用DriverManager类获取连接对象
*/

  @Test
  public void TestConnection6() throws Exception {
      //1.导入配置文件
      InputStream resourceAsStream = JDBCTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

      Properties properties = new Properties();
      properties.load(resourceAsStream);

      String url=properties.getProperty("url");
      String user=properties.getProperty("user");
      String password=properties.getProperty("password");
      String driverClass=properties.getProperty("driverClass");

      //2.加载驱动
      Class.forName(driverClass);

      //3.获取连接对象
      Connection connection = DriverManager.getConnection(url, user, password);
      System.out.println(connection);

  }
}

import org.apache.zookeeper.ZooKeeper;

public class zkCLient {

    private static String connectString =
            "hadoop102:2181,hadoop103:2181,hadoop104:2181";
    private static int sessionTimeout = 2000;
    private ZooKeeper zkClient = null;



}

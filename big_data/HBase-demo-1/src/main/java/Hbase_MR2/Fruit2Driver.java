package Hbase_MR2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Fruit2Driver implements Tool{

    //定义配置信息
    private Configuration configuration=null;

    public int run(String[] args) throws Exception {

        //1.获取job对象
        Job job = Job.getInstance();

        //2.设置主类路径
        job.setJarByClass(Fruit2Driver.class);

        //3.设置mapper路径及KV类型
        TableMapReduceUtil.initTableMapperJob(args[0],new Scan(),Fruit2Mapper.class,ImmutableBytesWritable.class, Put.class,job);

        //4.设置reducer路径&输出的表
        TableMapReduceUtil.initTableReducerJob(args[1],Fruit2Reducer.class,job);

        //5.提交job
        boolean result = job.waitForCompletion(true);

        return result?0:1;
    }

    public void setConf(Configuration conf) {
        configuration=conf;
    }

    public Configuration getConf() {
        return configuration;
    }

    public static void main(String[] args) {



        try {
            Configuration configuration=new Configuration();
            int run = ToolRunner.run(configuration, new Fruit2Driver(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

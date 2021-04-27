package getFruit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Fruit2FruitMRRunner implements Tool {

    Configuration configuration=this.getConf();

    public int run(String[] args) throws Exception {

        //创建Job任务
        Job job = Job.getInstance(configuration);

        //配置Job
        job.setJarByClass(Fruit2FruitMRRunner.class);

        //设置Mapper
        TableMapReduceUtil.initTableMapperJob(args[0],new Scan(),ReadFruitMapper.class,
                ImmutableBytesWritable.class, Put.class,job);

        //设置reducer
        TableMapReduceUtil.initTableReducerJob(args[1],WriteFruitMRReducer.class,job);

        //提交job
        boolean result = job.waitForCompletion(true);

        return result? 0:1;
    }

    public void setConf(Configuration conf) {
        conf=configuration;
    }

    public Configuration getConf() {
        return configuration;
    }

    public static void main(String[] args){
        Configuration configuration = new Configuration();
        try {
            ToolRunner.run(configuration,new Fruit2FruitMRRunner(),args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

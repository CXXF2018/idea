package partitionTest;

import flowsum.FlowBean;
import flowsum.FlowCountMapper;
import flowsum.FlowCountReducer;
import flowsum.FlowsumDriver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class PartitionDriver {
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        //配置信息，获取job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        //设置jar路径
        job.setJarByClass(FlowsumDriver.class);

        //设置map，reduce
        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReducer.class);

        //设置map端输出
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        //设置最终的输出
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //设置自定义分区以及相应的reduce任务数
        job.setPartitionerClass(ProvincePartitioner.class);
        job.setNumReduceTasks(5);

        //文件的输入输出路径
        FileInputFormat.setInputPaths(job,new Path("D:\\bigDataTestDirectory\\partitionTest"));
        FileOutputFormat.setOutputPath(job,new Path
                ("D:\\bigDataTestDirectory\\partitionTest_result"));

        //提交job
        boolean result = job.waitForCompletion(true);
        System.exit(result?0:1);
    }
}

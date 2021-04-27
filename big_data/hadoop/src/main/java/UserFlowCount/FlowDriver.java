package UserFlowCount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //设置三个class
        job.setJarByClass(FlowDriver.class);
        job.setMapperClass(FolwMapper.class);
        job.setReducerClass(FolwReducer.class);

        //设置map输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(User.class);

        //设置最终输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(User.class);

        //设置文件的输入输出路径
        FileInputFormat.setInputPaths(job,new Path("D:\\bigDataTestDirectory\\FlowCount"));
        FileOutputFormat.setOutputPath(job,new Path("D:\\bigDataTestDirectory\\FlowCount_result"));

        job.waitForCompletion(true);
    }
}

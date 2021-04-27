package getTopThree;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class TopThreeDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //设置Driver类Mapper和Reducer类
        job.setJarByClass(TopThreeDriver.class);
        job.setMapperClass(TopThreeMapper.class);
        job.setReducerClass(TopThreeReducer.class);

        //设置map输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //设置最终输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //设置输入输出路径
        FileInputFormat.setInputPaths(job,new Path("D:\\bigDataTestDirectory\\getTopThree"));
        FileOutputFormat.setOutputPath(job,new Path
                ("D:\\bigDataTestDirectory\\getTopThree_result"));

        job.waitForCompletion(true);
    }
}

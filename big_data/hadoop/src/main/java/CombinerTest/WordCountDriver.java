package CombinerTest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordCountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //获取配置信息，封装任务
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        //设置jar路径（driver）
        job.setJarByClass(WordCountDriver.class);

        //设置mapper，reducer路径
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        //设置文件输入方式
        job.setInputFormatClass(CombineTextInputFormat.class);

        //设置虚拟切片数
        CombineTextInputFormat.setMaxInputSplitSize(job,4194304);

        //设置map输出k,v类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //设置最终输出k,v类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setCombinerClass(WordcountCombiner.class);

        //设置输入输出路径
        FileInputFormat.setInputPaths(job,new Path("D:\\bigDataTestDirectory\\wordCounts"));
        FileOutputFormat.setOutputPath(job,new Path("D:\\bigDataTestDirectory\\combiner_result"));

        //提交job
        boolean result = job.waitForCompletion(true);
        System.exit(result? 0:1);
    }
}

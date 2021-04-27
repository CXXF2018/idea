package sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowsumDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //配置信息，获取job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        //设置jar路径
        job.setJarByClass(FlowsumDriver.class);

        //设置map，reduce
        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReducer.class);

        //设置map端输出
        job.setMapOutputKeyClass(FlowBeanSort.class);
        job.setMapOutputValueClass(Text.class);

        //设置最终的输出
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBeanSort.class);

        //文件的输入输出路径
        FileInputFormat.setInputPaths(job,new Path("D:\\bigDataTestDirectory\\seriliazble"));
        FileOutputFormat.setOutputPath(job,new Path
                ("D:\\bigDataTestDirectory\\comparable_result"));

        //提交job
        boolean result = job.waitForCompletion(true);
        System.exit(result?0:1);
    }
}

package CombinerTest;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordcountCombiner extends Reducer<Text, IntWritable, Text,IntWritable> {
//combiner 的父类是reducer，但是运行在MapTask节点上
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        // 1 汇总操作
        int count = 0;
        for(IntWritable v :values){
            count += v.get();
        }

        // 2 写出
        context.write(key, new IntWritable(count));
    }
}

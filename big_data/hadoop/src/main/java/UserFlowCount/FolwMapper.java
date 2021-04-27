package UserFlowCount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FolwMapper extends Mapper<LongWritable,Text,Text,User> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String s = value.toString();
        String[] split = s.split("\t");

        Text k = new Text();
        k.set(split[1]);
        User v = new User(Integer.parseInt(split[split.length-4]),Integer.parseInt(split[split
                .length-3]));

        context.write(k,v);
    }
}

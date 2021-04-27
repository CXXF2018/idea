package webLog;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LogMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    Text k = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        //获取一行
        String line = value.toString();

        //解析日志
        boolean result = parseLog(line,context);

        //判断日志是否合法
        if (!result){
            return;
        }

        k.set(line);

        context.write(k,NullWritable.get());

    }

    private boolean parseLog(String line, Context context) {
        String[] fields = line.split(" ");
        if (fields.length>11){
            //系统计数器
            context.getCounter("map","true").increment(1);
            return true;
        }else {
            context.getCounter("map","false").increment(1);
            return false;
        }

    }
}

package Order;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class OrderMapper extends Mapper<LongWritable,Text,orderBean,NullWritable> {

    orderBean orderBean = new orderBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1 获取一行
        String line = value.toString();

        // 2 截取
        String[] fields = line.split("\t");

        // 3 封装对象
        orderBean.setId(Integer.parseInt(fields[0]));
        orderBean.setPrice(Double.parseDouble(fields[2]));

        // 4 写出
        context.write(orderBean, NullWritable.get());

    }
}

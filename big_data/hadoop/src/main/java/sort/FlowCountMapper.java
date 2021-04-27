package sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowCountMapper extends Mapper<LongWritable,Text,FlowBeanSort,Text> {

    FlowBeanSort k = new FlowBeanSort();
    Text v=new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String line = value.toString();
        String[] fields = line.split("\t");

        //1	13736230513	192.196.100.1	www.atguigu.com	2481	24681	200
        //手机号
        String phoneNum=fields[1];

        //上行流量、下行流量
        long upFlow=Long.parseLong(fields[fields.length-3]);
        long downFlow=Long.parseLong(fields[fields.length-2]);


        k.set(upFlow,downFlow);
        v.set(phoneNum);

        //写出
        context.write(k,v);
    }
}

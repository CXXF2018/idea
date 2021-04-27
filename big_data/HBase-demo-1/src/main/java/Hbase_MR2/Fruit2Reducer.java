package Hbase_MR2;


import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Fruit2Reducer extends TableReducer<ImmutableBytesWritable,Put,NullWritable> {

    @Override
    protected void reduce(ImmutableBytesWritable key, Iterable<Put> puts, Context context) throws IOException, InterruptedException {

        //1.遍历写出
        for (Put put : puts) {

            context.write(NullWritable.get(),put);
        }
    }
}


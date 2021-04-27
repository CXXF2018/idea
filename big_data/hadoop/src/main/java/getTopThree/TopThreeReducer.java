package getTopThree;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopThreeReducer extends Reducer<Text,LongWritable,Text,LongWritable> {

    List<WebCount> webs = new ArrayList<WebCount>();

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        int topNum = conf.getInt("top", 3);

        Collections.sort(webs);
        int num = 0;
        for (WebCount web : webs) {
            num++;
            Text k = new Text(web.getWeb());
            LongWritable v = new LongWritable(web.getNum());
            context.write(k,v);
            if(num==topNum){
                return;
            }
        }
    }

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        LongWritable v = new LongWritable();
        for (LongWritable value : values) {
            sum+=value.get();
        }

        WebCount web = new WebCount(sum,key.toString());
        webs.add(web);
    }
}

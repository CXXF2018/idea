package UserFlowCount;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FolwReducer extends Reducer<Text,User,Text,User> {

    @Override
    protected void reduce(Text key, Iterable<User> values, Context context) throws IOException, InterruptedException {
        int sumUp = 0;
        int sumDown = 0;
        for (User value : values) {
            sumDown += value.getDownFlow();
            sumUp += value.getUpFlow();
        }

        User v = new User(sumUp, sumDown);

        context.write(key,v);
    }
}

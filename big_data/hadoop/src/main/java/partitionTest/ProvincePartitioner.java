package partitionTest;

import flowsum.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class ProvincePartitioner extends Partitioner<Text, FlowBean> {

    public int getPartition(Text text, FlowBean flowBean, int i) {
        String s = text.toString().substring(0, 3);

        int partition=4;
        if ("136".equals(s)) {
            partition = 0;

        } else if ("137".equals(s)) {
            partition = 1;

        } else if ("138".equals(s)) {
            partition = 2;

        } else if ("139".equals(s)) {
            partition = 3;

        }

        return partition;
    }
}

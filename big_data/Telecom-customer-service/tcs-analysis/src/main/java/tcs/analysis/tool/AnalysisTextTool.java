package tcs.analysis.tool;

import com.tcs.common.constant.Names;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import tcs.analysis.io.MySQLTextOutputFormat;
import tcs.analysis.mapper.AnalysisTextMapper;
import tcs.analysis.reducer.AnalysisTextReducer;

//分析数据的工具类
public class AnalysisTextTool implements Tool {

    public int run(String[] args) throws Exception {

        Job job = Job.getInstance();
        job.setJarByClass(AnalysisTextTool.class);

        //map
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(Names.CF_CALLEE.getvalue()));

        TableMapReduceUtil.initTableMapperJob(Names.TABLENAME.getvalue(),scan, AnalysisTextMapper
                .class,Text.class,Text.class,job);

        //reduce
        job.setReducerClass(AnalysisTextReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //outputformat
        job.setOutputFormatClass(MySQLTextOutputFormat.class);



        boolean flag= job.waitForCompletion(true);
        return flag==true? 1:0;
    }

    public void setConf(Configuration conf) {

    }

    public Configuration getConf() {
        return null;
    }
}

package OutputFormat;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class FilterRecordWriter extends RecordWriter<Text,NullWritable> {

    FSDataOutputStream atguiguOut=null;
    FSDataOutputStream othersOut=null;

    public FilterRecordWriter(TaskAttemptContext job) {

        //获取文件系统
        FileSystem fs;

        try {
            fs=FileSystem.get(job.getConfiguration());

            //创建输出文件路径
            Path atguiguPath = new Path
                    ("D:\\bigDataTestDirectory\\outPutFormatTest_file\\atguigu.log");
            Path othersPath = new Path("D:\\bigDataTestDirectory\\outPutFormatTest_file\\other" +
                    ".log");

            //创建输出流
            atguiguOut = fs.create(atguiguPath);
            othersOut = fs.create(othersPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(Text text, NullWritable nullWritable) throws IOException, InterruptedException {
        if (text.toString().contains("atguigu")){
            atguiguOut.write(text.toString().getBytes());
        }else {
            othersOut.write(text.toString().getBytes());
        }
    }

    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        IOUtils.closeStream(atguiguOut);
        IOUtils.closeStream(othersOut);
    }
}

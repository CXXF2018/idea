package MyFileInputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class whileRecordReader extends RecordReader<Text,BytesWritable>{

    private Text key=new Text();
    private BytesWritable value=new BytesWritable();
    private FileSplit split;
    private Configuration configuration;
    boolean isProgress=true;

    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        configuration=taskAttemptContext.getConfiguration();
        this.split=(FileSplit)inputSplit;
    }

    public boolean nextKeyValue() throws IOException, InterruptedException {

        if (isProgress) {
            FileSystem fs=null;
            FSDataInputStream fis=null;
            byte[] contents = new byte[(int) split.getLength()];

            try {
                //获取文件路径和文件系统
                Path path=split.getPath();
                fs = path.getFileSystem(configuration);

                //获取输入流
                fis=fs.open(path);

                //读取文件中的内容
                IOUtils.readFully(fis,contents,0,contents.length);

                //输出文件内容
                value.set(contents,0,contents.length);

                //设置k值
                key.set(path.toString());

            } catch (IOException e) {

            } finally {
                IOUtils.closeStream(fis);
            }

            isProgress=false;
            return true;
        }

        return false;
    }

    public Text getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    public void close() throws IOException {

    }
}

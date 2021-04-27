package tcs.analysis.io;

import com.tcs.common.util.JDBCUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


//MySQL数据格式化输出
public class MySQLTextOutputFormat extends OutputFormat<Text,Text> {

    protected static class MySQLRecordWriter extends RecordWriter<Text, Text> {

        //获取资源
        private Connection connection= null;

        private Map<String,Integer> userMap=new HashMap<String,Integer>();
        private Map<String,Integer> dateMap=new HashMap<String,Integer>();


        public MySQLRecordWriter(){
            connection= JDBCUtil.getConnection();

            //读取用户，时间数据
            String queryUserSql="select id,tel from tcs_user";

            PreparedStatement statement=null;
            ResultSet resultSet=null;

            try {
                statement=connection.prepareStatement(queryUserSql);
                resultSet = statement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                if(statement!=null){

                    if(resultSet!=null){
                        try {
                            resultSet.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        }



        //输出数据
        public void write(Text key, Text value) throws IOException, InterruptedException {

            String[] values=value.toString().split("_");
            String sumCall=values[0];
            String sumDuration=values[1];

            PreparedStatement pstat=null;

            try {
                String insertSQL="insert into tcs_call(telid,dateid,sumcall,sumduration) values(?,?," +
                        "?,?);";

                pstat=connection.prepareStatement(insertSQL);
                pstat.setInt(1,2);
                pstat.setInt(2,3);
                pstat.setInt(3,Integer.parseInt(sumCall));
                pstat.setInt(4,Integer.parseInt(sumDuration));
                pstat.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                if(pstat!=null){
                    try {
                        pstat.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //释放资源
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {

            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public RecordWriter<Text, Text> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
        return new MySQLRecordWriter();
    }

    public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {

    }


    private FileOutputCommitter committer = null;

    public static Path getOutputPath(JobContext job) {
        String name = job.getConfiguration().get(FileOutputFormat.OUTDIR);
        return name == null ? null: new Path(name);
    }

    public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
        if (committer == null) {
            Path output = getOutputPath(context);
            committer = new FileOutputCommitter(output, context);
        }
        return committer;
    }
}

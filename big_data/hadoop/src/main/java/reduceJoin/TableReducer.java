package reduceJoin;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class TableReducer extends Reducer<Text, TableBean, TableBean, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<TableBean> values, Context context) throws IOException, InterruptedException {

       //存储订单的集合
        ArrayList<TableBean> orderBean = new ArrayList<TableBean>();
        TableBean pdBean = new TableBean();

        for (TableBean value : values) {
            TableBean tmpBean=new TableBean();
            if ("order".equals(value.getFlag())){
                try {
                    BeanUtils.copyProperties(tmpBean,value);
                    orderBean.add(tmpBean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    BeanUtils.copyProperties(pdBean,value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        //拼接表
        for (TableBean tableBean : orderBean) {
            tableBean.setPname(pdBean.getPname());
            context.write(tableBean,NullWritable.get());
        }
    }
}

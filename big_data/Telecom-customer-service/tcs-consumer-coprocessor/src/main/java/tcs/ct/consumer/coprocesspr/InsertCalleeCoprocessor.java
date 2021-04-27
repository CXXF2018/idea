package tcs.ct.consumer.coprocesspr;

import com.tcs.common.bean.BaseDao;
import com.tcs.common.constant.Names;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

//使用协处理器保存被叫用户的数据


/*协处理器的使用
* 1.创建类
* 2.让我们的表知道协处理器类（和表有关联）*/
public class InsertCalleeCoprocessor extends BaseRegionObserver {

    //方法的命名
    //preput
    //doput:模板方法设计模式
    //      存在父子类
    //      父类搭建算法骨架
    //      1.tel获取用户代码 2.时间取年月 3.异或运算  4.hash散列
    //      子类重写算法的细节
    //      do1.获取tel后四位  do2.202008  do3. ^ do4. % &
    //postput

    //保存主叫用户数据后，hbase自动保存被叫用户数据
    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        //获取表
        Table table = e.getEnvironment().getTable(TableName.valueOf(Names.TABLENAME.getvalue()));

        //获取rowkey
        String rowkey=Bytes.toString(put.getRow());

        //1_133_2020_144_1010_1
        String[] values=rowkey.split("_");
        String call1=values[1];
        String call2=values[3];
        String calltime=values[2];
        String duration=values[4];
        String flag=values[5];

        if("1".equals(flag)) {

            //只有主叫用户保存后才需要触发被叫用户的保存
            CoprocessorDao dao = new CoprocessorDao();
            String calleeRowkey = dao.getRegionNum(call2, calltime)
                    + "_" + call2 + "_" + calltime + "_" + call1 + "_" + duration + "_0";

            //保存数据
            //被叫用户
            Put calleeput = new Put(Bytes.toBytes(calleeRowkey));

            calleeput.addColumn(Bytes.toBytes(Names.CF_CALLEE.getvalue()), Bytes.toBytes("call2"),
                    Bytes.toBytes(call2));
            calleeput.addColumn(Bytes.toBytes(Names.CF_CALLEE.getvalue()), Bytes.toBytes("call1"),
                    Bytes.toBytes(call1));
            calleeput.addColumn(Bytes.toBytes(Names.CF_CALLEE.getvalue()), Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
            calleeput.addColumn(Bytes.toBytes(Names.CF_CALLEE.getvalue()), Bytes.toBytes("duration"), Bytes.toBytes(duration));
            calleeput.addColumn(Bytes.toBytes(Names.CF_CALLEE.getvalue()), Bytes.toBytes("flag"), Bytes
                    .toBytes("0"));

            table.put(calleeput);

            //关闭表
            table.close();
        }
    }

    private class CoprocessorDao extends BaseDao{
        public int getRegionNum(String tel,String time){
            return genRegionNum(tel,time);
        }
    }
}

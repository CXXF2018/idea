package sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FlowBeanSort implements WritableComparable<FlowBeanSort> {

    private long upFlow;
    private long downFlow;
    private long sumFlow;

    //空参构造用于反序列化
    public FlowBeanSort() {
    }

    public FlowBeanSort(long upFlow, long downFlow) {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow=upFlow+downFlow;
    }

    //重写序列化方法
    public void write(DataOutput out) throws IOException {
        out.writeLong(upFlow);
        out.writeLong(downFlow);
        out.writeLong(sumFlow);
    }

    //重写反序列化方法
    public void readFields(DataInput in) throws IOException {
        this.upFlow = in.readLong();
        this.downFlow=in.readLong();
        this.sumFlow=in.readLong();
    }

    //重写toString方法

    @Override
    public String toString() {
        return "FlowBean{" +
                "upFlow=" + upFlow +
                ", downFlow=" + downFlow +
                ", sumFlow=" + sumFlow +
                '}';
    }

    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(long sumFlow) {
        this.sumFlow = sumFlow;
    }

    public void set(long upFlow,long downFlow){
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow=upFlow+downFlow;
    }

    public int compareTo(FlowBeanSort o) {

        int result;
        if (sumFlow>o.sumFlow){
            result=1;
        }else if (sumFlow<o.sumFlow) {
            result = -1;
        }else {
            result=0;
        }

        return result;
    }
}

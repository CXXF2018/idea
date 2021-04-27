package Order;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class OrderSortGroupingComparator extends WritableComparator{

    public OrderSortGroupingComparator() {
        super(orderBean.class, true);//此处不可不写
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {

        orderBean aBean = (orderBean)a;
        orderBean bBean=(orderBean)b;

        int result;
        if (aBean.getId()>bBean.getId()){
            result=1;
        }else if (aBean.getId()<bBean.getId()){
            result=-1;
        }else {
            result=0;
        }

        return result;
    }
}

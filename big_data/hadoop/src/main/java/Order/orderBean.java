package Order;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class orderBean implements WritableComparable<orderBean> {

    private int id;
    private double price;

    public orderBean() {
    }

    public orderBean(int id, double price) {
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int compareTo(orderBean o) {
        int result;
        if (id>o.getId()){
            result=1;
        }else if (id<o.getId()){
            result=-1;
        }else {
            result=price>o.getPrice()? -1:1;
        }

        return result;
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(id);
        out.writeDouble(price);

    }

    public void readFields(DataInput in) throws IOException {
        id=in.readInt();
        price=in.readDouble();
    }

    @Override
    public String toString() {
        return "orderBean{" +
                "id=" + id +
                ", price=" + price +
                '}';
    }
}

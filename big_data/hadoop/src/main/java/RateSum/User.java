package RateSum;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class User implements WritableComparable<User> {

    private String movie;
    private int rate;
    private String timeStamp;
    private int uid;

    public User() {
    }

    public User(String movie, int rate, String timeStamp, int uid) {
        this.movie = movie;
        this.rate = rate;
        this.timeStamp = timeStamp;
        this.uid = uid;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "User{" +
                "movie='" + movie + '\'' +
                ", rate=" + rate +
                ", timeStamp=" + timeStamp +
                ", uid=" + uid +
                '}';
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(movie);
        out.writeInt(rate);
        out.writeUTF(timeStamp);
        out.writeInt(uid);
    }

    public void readFields(DataInput in) throws IOException {
        movie = in.readUTF();
        rate = in.readInt();
        timeStamp = in.readUTF();
        uid = in.readInt();
    }

    public int compareTo(User o) {
        return rate-o.getRate();
    }
}

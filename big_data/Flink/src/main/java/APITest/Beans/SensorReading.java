package APITest.Beans;

//传感器温度读数的数据类型
public class SensorReading {

    //参数：属性Id，时间戳，温度值
    private String Id;
    private Long timeStamp;
    private Double temperature;

    public SensorReading() {
    }

    public SensorReading(String id, Long timeStamp, Double temperature) {
        Id = id;
        this.timeStamp = timeStamp;
        this.temperature = temperature;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "Id='" + Id + '\'' +
                ", timeStamp=" + timeStamp +
                ", temperature=" + temperature +
                '}';
    }
}

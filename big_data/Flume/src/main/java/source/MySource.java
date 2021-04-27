package source;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;

import java.util.HashMap;

public class MySource extends AbstractSource implements Configurable,PollableSource {

    //定义配置文件将来要读取的字段
    private Long delay;
    private String field;

    public Status process() throws EventDeliveryException {

        try {
            //创建事件头信息
            HashMap<String, String> headerMap = new HashMap<String, String>();

            //创建事件
            SimpleEvent event = new SimpleEvent();

            //封装事件
            for (int i = 0; i < 5; i++) {
                event.setHeaders(headerMap);

                event.setBody((field+i).getBytes());

                getChannelProcessor().processEvent(event);
                Thread.sleep(delay);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Status.BACKOFF;
        }

        return Status.READY;
    }

    public long getBackOffSleepIncrement() {
        return 0;
    }

    public long getMaxBackOffSleepInterval() {
        return 0;
    }

    //初始化配置信息
    public void configure(Context context) {
        delay=context.getLong("delay");
        field=context.getString("field","Hello!");
    }
}

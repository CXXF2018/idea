package interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;

public class CustomInterceptor implements Interceptor {
    public void initialize() {

    }

    public Event intercept(Event event) {

        byte[] body = event.getBody();
        if (body[0]>='a'&&body[0]<='z'){
            event.getHeaders().put("type","letter");
        }else {
            event.getHeaders().put("type","number");
        }
        return event;
    }

    public List<Event> intercept(List<Event> list) {

        for (Event event : list) {
            intercept(event);
        }
        return list;
    }

    public void close() {

    }

    public static class Builder implements Interceptor.Builder{

        public Interceptor build() {
            return new CustomInterceptor();
        }

        public void configure(Context context) {

        }
    }
}

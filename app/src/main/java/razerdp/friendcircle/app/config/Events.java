package razerdp.friendcircle.app.config;

/**
 * Created by 大灯泡 on 2016/3/7.
 * eventbus使用的event
 */
public class Events {

    private Object event;

    public Events(Object event) {
        this.event = event;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }

    //通知更新
    public static class CallToRefresh {
        public boolean needRefresh = true;

        public CallToRefresh(boolean needRefresh) {
            this.needRefresh = needRefresh;
        }
    }
}

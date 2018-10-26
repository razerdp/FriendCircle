package razerdp.friendcircle.app.manager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 大灯泡 on 2018/10/26.
 */
public class EventBusManager {
    public static void register(Object object) {
        try {
            if (!EventBus.getDefault().isRegistered(object)) {
                EventBus.getDefault().register(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregister(Object object) {
        try {
            if (EventBus.getDefault().isRegistered(object)) {
                EventBus.getDefault().unregister(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void post(Object event) {
        try {
            EventBus.getDefault().post(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

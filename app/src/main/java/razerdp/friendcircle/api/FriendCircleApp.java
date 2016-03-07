package razerdp.friendcircle.api;

import android.app.Application;
import android.content.Context;
import razerdp.friendcircle.api.network.base.VolleyManager;

/**
 * Created by 大灯泡 on 2016/2/24.
 * 项目部署
 */
public class FriendCircleApp extends Application {
    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        VolleyManager.INSTANCE.initQueue(10*1024*1024);
    }

    public static String getRootUrl(){
        return "http://115.28.102.139:8080/myserver";
    }
}

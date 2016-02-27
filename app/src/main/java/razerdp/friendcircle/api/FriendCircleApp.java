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

    //是否在宿舍。。。。
    public static boolean isAtHome=false;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        VolleyManager.INSTANCE.initQueue(10*1024*1024);
    }

    public static String getRootUrl(){
        return isAtHome?"http://192.168.199.105":"http://10.0.0.165";
    }
}

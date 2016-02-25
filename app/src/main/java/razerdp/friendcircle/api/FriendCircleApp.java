package razerdp.friendcircle.api;

import android.app.Application;
import android.content.Context;

/**
 * Created by 大灯泡 on 2016/2/24.
 * 项目部署
 */
public class FriendCircleApp extends Application {
    public static Context appContext;

    public static boolean isLocalServer=true;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static String getRootUrl(){
        return isLocalServer?"127.0.0.1/momentsList":"";
    }
}

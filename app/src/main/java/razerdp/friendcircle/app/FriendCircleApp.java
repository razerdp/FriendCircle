package razerdp.friendcircle.app;

import android.app.Application;
import android.content.Context;
import razerdp.friendcircle.app.config.LocalHostInfo;
import razerdp.friendcircle.app.https.base.VolleyManager;

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
        VolleyManager.INSTANCE.initQueue(10 * 1024 * 1024);

        LocalHostInfo.INSTANCE.setHostId(1001);
        LocalHostInfo.INSTANCE.setHostNick("羽翼君");
        LocalHostInfo.INSTANCE.setHostAvatar("http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg");
    }

    public static String getRootUrl() {
        return "http://115.28.102.139:8080/myserver";
    }
}

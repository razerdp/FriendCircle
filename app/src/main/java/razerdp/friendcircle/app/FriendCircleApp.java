package razerdp.friendcircle.app;

import android.app.Application;
import android.content.Context;
import razerdp.friendcircle.app.config.LocalHostInfo;
import razerdp.friendcircle.app.https.base.VolleyManager;
import razerdp.friendcircle.utils.PreferenceUtils;

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
        PreferenceUtils.INSTANCE.init(appContext,"Friendpref",MODE_PRIVATE);

        LocalHostInfo.INSTANCE.setHostId(1001);
        LocalHostInfo.INSTANCE.setHostNick("羽翼君");
        LocalHostInfo.INSTANCE.setHostAvatar("http://upload.jianshu.io/users/upload_avatars/684042/bd1b2f796e3a.jpg");
    }

    public static String getRootUrl() {
        return "因为在下的服务器受到了某次攻击，明明是个小项目，却也要攻击，真是日了狗了。" +
                "所以，如果您需要测试数据，请简信羽翼君或者加QQ(164701463)问我拿" +
                "另外，攻击的hacker，，，请留手吧，这服务器仅仅是学生价租的一个小服务器，没有任何价值";
    }
}

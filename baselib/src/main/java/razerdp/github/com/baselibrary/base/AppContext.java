package razerdp.github.com.baselibrary.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * Created by 大灯泡 on 2017/3/22.
 * <p>
 * baselib appcontext类
 * <p>
 * 该工具类方法来自：
 * https://github.com/kymjs/Common/blob/master/Common/common/src/main/java/com/kymjs/common/App.java
 */

public class AppContext {

    private static final String TAG = "AppContext";
    public static final Application sApplication;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Log.e(TAG, "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e(TAG, "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            sApplication = app;
        }
    }

    private static void checkAppContext() {
        if (sApplication == null)
            throw new IllegalStateException("app reference is null");
    }


    public static Application getAppInstance() {
        checkAppContext();
        return sApplication;
    }

    public static Context getAppContext() {
        checkAppContext();
        return sApplication.getApplicationContext();
    }

    public static Resources getResources() {
        checkAppContext();
        return sApplication.getResources();
    }

}

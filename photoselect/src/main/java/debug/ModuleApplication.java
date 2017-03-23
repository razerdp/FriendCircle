package debug;

import android.app.Application;

import razerdp.github.com.baselibrary.helper.AppFileHelper;

/**
 * Created by 大灯泡 on 2017/3/22.
 * <p>
 * module的application
 */

public class ModuleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppFileHelper.initStoryPath();
    }
}

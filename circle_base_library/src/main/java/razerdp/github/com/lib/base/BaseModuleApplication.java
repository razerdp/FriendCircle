package razerdp.github.com.lib.base;

import android.app.Application;

import razerdp.github.com.lib.api.AppContext;
import razerdp.github.com.lib.manager.localphoto.LocalPhotoManager;

/**
 * Created by 大灯泡 on 2017/4/1.
 * <p>
 * module的application父类..主要用来初始ARouter等
 */

public class BaseModuleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.initARouter();
        LocalPhotoManager.INSTANCE.registerContentObserver(null);
    }

}

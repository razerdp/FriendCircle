package debug;

import android.app.Application;

import razerdp.github.com.baselibrary.helper.AppFileHelper;
import razerdp.github.com.baselibrary.helper.AppSetting;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;

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
        LocalPhotoManager.INSTANCE.registerContentObserver(null);
        if (AppSetting.loadBooleanPreferenceByKey(AppSetting.APP_HAS_SCAN_IMG, false)) {
            LocalPhotoManager.INSTANCE.scanImgAsync(null);
        }
    }

    @Override
    public void onTerminate() {
        LocalPhotoManager.INSTANCE.writeToLocal();
        super.onTerminate();
    }
}

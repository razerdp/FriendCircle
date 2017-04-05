package razerdp.github.com.baselibrary.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import razerdp.github.com.baselibrary.helper.AppFileHelper;
import razerdp.github.com.baselibrary.helper.AppSetting;
import razerdp.github.com.baselibrary.manager.localphoto.LocalPhotoManager;

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
        AppFileHelper.initStoryPath();
        LocalPhotoManager.INSTANCE.registerContentObserver(null);
        if (AppSetting.loadBooleanPreferenceByKey(AppSetting.APP_HAS_SCAN_IMG, false)) {
            LocalPhotoManager.INSTANCE.scanImgAsync(null);
        }
    }

}

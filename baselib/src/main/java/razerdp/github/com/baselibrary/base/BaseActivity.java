package razerdp.github.com.baselibrary.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

/**
 * Created by 大灯泡 on 2017/3/22.
 * <p>
 * BaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean isAppInBackground = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onHandleIntent(getIntent());
    }


    @Override
    protected void onStop() {
        super.onStop();
//        if (isApplicationInBackground()) {
//            isAppInBackground = true;
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (isAppInBackground) {
//            isAppInBackground = false;
//        }
    }

    /**
     * run in {@link BaseActivity#onCreate(Bundle)} but before {@link AppCompatActivity#setContentView(int)}
     * <p>
     * <p>
     * 如果有intent，则需要处理这个intent（该方法在onCreate里面执行，但在setContentView之前调用）
     *
     * @param intent
     * @return false:关闭activity
     */
    public abstract void onHandleIntent(Intent intent);

    protected <T extends View> T findView(@IdRes int id) {
        return (T) super.findViewById(id);
    }

    private boolean isApplicationInBackground() {
        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            final ComponentName topActivity = tasks.get(0).topActivity;
            return !topActivity.getPackageName().equals(getPackageName());
        }
        return false;
    }

}

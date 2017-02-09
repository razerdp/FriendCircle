package razerdp.friendcircle.ui.base;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by 大灯泡 on 2016/10/28.
 *
 * baseactivity
 */

public class BaseActivity extends AppCompatActivity {


    /**
     * 判断程序是否在前台运行
     */
    protected boolean isTopApp(Context context) {
        String packageName = "razerdp.friendcircle";
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}

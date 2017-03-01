package razerdp.friendcircle.ui.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.socks.library.KLog;

import java.util.List;

import razerdp.friendcircle.R;
import razerdp.friendcircle.app.interfaces.MultiClickListener;
import razerdp.friendcircle.ui.widget.common.TitleBar;

import static razerdp.friendcircle.ui.widget.common.TitleBar.*;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * baseactivity
 */

public class BaseActivity extends AppCompatActivity {
    protected TitleBar titleBar;
    protected boolean isAppInBackground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i("activityName", "now on activity  >>>  " + this.getClass().getSimpleName());
    }

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

    @Override
    protected void onStop() {
        super.onStop();
        if (isApplicationInBackground()) {
            isAppInBackground = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAppInBackground) {
            isAppInBackground = false;
        }
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initTitlebar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initTitlebar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initTitlebar();
    }

    private void initTitlebar() {
        if (titleBar == null) titleBar = (TitleBar) findViewById(R.id.title_bar_view);
        if (titleBar != null) {
            titleBar.setOnClickListener(new MultiClickListener() {
                @Override
                public void onSingleClick() {
                    onTitleSingleClick();
                }

                @Override
                public void onDoubleClick() {
                    onTitleDoubleClick();
                }
            });
            titleBar.setOnTitleBarClickListener(onTitleClickListener);
        }
    }


    // titlebar相关事件
    private OnTitleBarClickListener onTitleClickListener = new OnTitleBarClickListener() {

        @Override
        public boolean onLeftClick(View v, boolean isLongClick) {
            if (!isLongClick) {
                onTitleLeftClick();
                return false;
            } else {
                return onTitleLeftLongClick();
            }
        }

        @Override
        public boolean onRightClick(View v, boolean isLongClick) {
            if (!isLongClick) {
                onTitleRightClick();
                return false;
            } else {
                return onTitleRightLongClick();
            }
        }
    };

    public boolean onTitleLeftLongClick() {
        return false;
    }

    public boolean onTitleRightLongClick() {
        return false;
    }

    public void onTitleLeftClick() {
        finish();
    }

    public void onTitleRightClick() {
    }

    public void onTitleDoubleClick() {
    }

    public void onTitleSingleClick() {
    }

    public void setTitle(int resId) {
        if (titleBar != null && resId != 0) {
            titleBar.setTitle(resId);
        }
    }

    public void setTitle(String title) {
        if (titleBar != null && !TextUtils.isEmpty(title)) {
            titleBar.setTitle(title);
        }
    }

    public void setTitleMode(@TitleBar.TitleBarMode int mode) {
        if (titleBar != null) {
            titleBar.setTitleBarMode(mode);
        }
    }

    public void setTitleRightText(String text) {
        if (titleBar != null) {
            titleBar.setRightText(text);
        }
    }

    public void setTitleRightIcon(int resid) {
        if (titleBar != null) {
            titleBar.setRightIcon(resid);
        }
    }

    public void setTitleLeftText(String text) {
        if (titleBar != null) {
            titleBar.setLeftText(text);
        }
    }

    public void setTitleLeftIcon(int resid) {
        if (titleBar != null) {
            titleBar.setLeftIcon(resid);
        }
    }

    public void setLeftTextColor(int color) {
        if (titleBar != null) {
            titleBar.setLeftTextColor(color);
        }
    }

    public void setRightTextColor(int color) {
        if (titleBar != null) {
            titleBar.setRightTextColor(color);
        }
    }

    public void setTitleBarBackground(int color) {
        if (titleBar != null) {
            titleBar.setTitleBarBackground(color);
        }
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

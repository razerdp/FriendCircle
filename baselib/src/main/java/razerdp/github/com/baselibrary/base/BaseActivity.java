package razerdp.github.com.baselibrary.base;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.socks.library.KLog;

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
        KLog.i("activity onCreate :  " + this.getClass().getSimpleName());
        ARouter.getInstance().inject(this);
        onHandleIntent(getIntent());
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (AppContext.isAppBackground()) {
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


    public Activity getActivity() {
        return BaseActivity.this;
    }

    /**
     * 隐藏状态栏
     * <p>
     * 在setContentView前调用
     */
    protected void hideStatusBar() {
        final int sdkVer = Build.VERSION.SDK_INT;
        if (sdkVer < 16) {
            //4.0及一下
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

}

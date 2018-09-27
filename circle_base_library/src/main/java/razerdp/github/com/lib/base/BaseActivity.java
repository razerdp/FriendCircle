package razerdp.github.com.lib.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.socks.library.KLog;

import razerdp.github.com.lib.api.AppContext;
import razerdp.github.com.lib.helper.PermissionHelper;
import razerdp.github.com.lib.interfaces.IPermission;
import razerdp.github.com.lib.interfaces.OnPermissionGrantListener;

/**
 * Created by 大灯泡 on 2017/3/22.
 * <p>
 * BaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity implements IPermission {

    private PermissionHelper mPermissionHelper;

    protected boolean isAppInBackground = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i("当前打开 :  " + this.getClass().getSimpleName());
        if (mPermissionHelper == null) {
            mPermissionHelper = new PermissionHelper(this);
        }
        ARouter.getInstance().inject(this);
        onHandleIntent(getIntent());
    }

    public void requestPermission(OnPermissionGrantListener listener, PermissionHelper.Permission... permissions) {
        getPermissionHelper().requestPermission(listener, permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handlePermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public PermissionHelper getPermissionHelper() {
        if (mPermissionHelper == null) {
            mPermissionHelper = new PermissionHelper(this);
        }
        return mPermissionHelper;
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
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    protected void showStatusBar() {
        final int sdkVer = Build.VERSION.SDK_INT;
        if (sdkVer < 16) {
            //4.0及一下
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPermissionHelper != null) {
            mPermissionHelper.handleDestroy();
        }
        mPermissionHelper = null;
    }

}

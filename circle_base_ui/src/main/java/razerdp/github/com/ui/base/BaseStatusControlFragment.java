package razerdp.github.com.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import razerdp.github.com.baseuilib.R;
import razerdp.github.com.lib.base.BaseFragment;
import razerdp.github.com.ui.helper.StatusBarHelper;
import razerdp.github.com.ui.widget.systembar.StatusBarViewPlaceHolder;

/**
 * Created by 大灯泡 on 2019/1/16.
 */
public abstract class BaseStatusControlFragment extends BaseFragment {
    protected StatusBarViewPlaceHolder mStatusBarViewPlaceHolder;
    private boolean isInitStatusConfig = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void afterInitView() {
        super.afterInitView();
        if (!isInitStatusConfig && isTranslucentStatus()) {
            StatusBarHelper.setRootViewFitsSystemWindows(getActivity(), isFitsSystemWindows());
            StatusBarHelper.setTranslucentStatus(getActivity());
            isInitStatusConfig = true;
        }
        if (mStatusBarViewPlaceHolder == null && getRootView() != null) {
            mStatusBarViewPlaceHolder = getRootView().findViewById(R.id.statusbar_placeholder);
        }
    }

    protected void setStatusBarHolderBackgroundColor(int color) {
        if (mStatusBarViewPlaceHolder != null) {
            mStatusBarViewPlaceHolder.setBackgroundColor(color);
        }
    }

    protected boolean isFitsSystemWindows() {
        return true;
    }

    protected boolean isTranslucentStatus() {
        return false;
    }

    protected void setStatusBarColor(int color) {
        StatusBarHelper.setStatusBarColor(getActivity(), color);
    }

    protected boolean setStatusBarDark(boolean dark) {
        return StatusBarHelper.setStatusBarDarkTheme(getActivity(), dark);
    }
}

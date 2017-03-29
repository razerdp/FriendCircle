package razerdp.github.com.baselibrary.base;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.logging.Logger;

/**
 * Created by 大灯泡 on 2017/3/29.
 * <p>
 * basefragment
 */

public abstract class BaseFragment extends Fragment {
    private View rootView;
    protected Activity mActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null) {
            mActivity = getActivity();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mActivity == null) {
            mActivity = getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mActivity == null) {
            mActivity = getActivity();
        }
        rootView = inflater.inflate(getLayoutResId(), container, false);
        if (rootView != null) {
            onInitData();
            onInitView(rootView);
            return rootView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        onVisibleChange(isVisibleToUser);
    }

    @LayoutRes
    public abstract int getLayoutResId();

    protected abstract void onInitData();

    protected abstract void onInitView(View rootView);

    protected void onVisibleChange(boolean isVisibleToUser) {
    }

    protected View getRootView() {
        return rootView;
    }

    protected <T extends View> T findView(@IdRes int id) {
        if (rootView != null) {
            return (T) rootView.findViewById(id);
        } else {
            return null;
        }
    }
}

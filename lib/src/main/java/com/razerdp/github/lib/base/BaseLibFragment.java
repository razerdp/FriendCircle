package com.razerdp.github.lib.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razerdp.github.lib.helper.PermissionHelper;
import com.razerdp.github.lib.interfaces.IPermission;
import com.razerdp.github.lib.interfaces.OnPermissionGrantListener;
import com.razerdp.github.lib.utils.KeyBoardUtil;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by 大灯泡 on 2017/3/29.
 * <p>
 * basefragment
 */

public abstract class BaseLibFragment extends Fragment implements IPermission {
    private View rootView;
    protected Activity mActivity;
    private PermissionHelper mPermissionHelper;

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
        if (mPermissionHelper == null) {
            mPermissionHelper = new PermissionHelper(this);
        }
        rootView = inflater.inflate(getLayoutResId(), container, false);
        if (rootView != null) {
            onPreInitView(rootView);
            onInitData();
            onInitView(rootView);
            afterInitView();
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

    public void requestPermission(OnPermissionGrantListener listener, PermissionHelper.Permission... permissions) {
        getPermissionHelper().requestPermission(listener, permissions);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPermissionHelper != null) {
            mPermissionHelper.handleDestroy();
        }
        mPermissionHelper = null;
    }

    @LayoutRes
    public abstract int getLayoutResId();

    protected void onPreInitView(View rootView) {
    }


    protected abstract void onInitData();

    protected abstract void onInitView(View rootView);

    protected void afterInitView() {
    }

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

    protected void back() {
        KeyBoardUtil.close(getActivity());
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            getActivity().finish();
        }
    }
}

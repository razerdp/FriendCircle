package com.razerdp.github.uilib.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razerdp.github.lib.utils.KLog;
import com.razerdp.github.lib.utils.KeyBoardUtil;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 大灯泡 on 2017/3/29.
 * <p>
 * basefragment
 */

public abstract class BaseLibFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();
    protected View mRootView;
    protected Context mContext;
    private Unbinder mUnbinder;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        if (getArguments() != null) {
            onHandleArguments(getArguments());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mContext == null) {
            mContext = requireContext();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            KLog.i(TAG, "打开Fragment ： " + TAG);
            mRootView = inflater.inflate(layoutId(), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);
            onInitView(mRootView);
            afterInitView();
        }
        return mRootView;
    }

    protected void onHandleArguments(@NonNull Bundle arguments) {

    }

    @LayoutRes
    public abstract int layoutId();

    protected abstract void onInitView(View rootView);

    protected void afterInitView() {
    }

    protected View getRootView() {
        return mRootView;
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        if (mRootView != null) {
            return (T) mRootView.findViewById(id);
        } else {
            return null;
        }
    }

    public final void finishActivity() {
        KeyBoardUtil.close(getActivity());
        try {
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void back() {
        KeyBoardUtil.close(getActivity());
        if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            finishActivity();
        }
    }
}

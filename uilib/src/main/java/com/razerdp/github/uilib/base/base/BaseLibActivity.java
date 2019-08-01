package com.razerdp.github.uilib.base.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.razerdp.github.lib.interfaces.IPermission;
import com.razerdp.github.lib.utils.KLog;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by 大灯泡 on 2017/3/22.
 * <p>
 * BaseLibActivity
 */

public abstract class BaseLibActivity extends AppCompatActivity implements IPermission {
    protected final String TAG = getClass().getSimpleName();

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i("当前打开 :  ", TAG);
        onHandleIntent(getIntent());
        if (layoutId() != 0) {
            setContentView(layoutId());
        }
    }

    public void onHandleIntent(Intent intent) {

    }

    @LayoutRes
    public abstract int layoutId();

    protected abstract void onInitView(View rootView);

    public BaseLibActivity getContext() {
        return BaseLibActivity.this;
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onAttachContentView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onAttachContentView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        onAttachContentView();
    }

    protected void onAttachContentView() {
        //防止多次调用onInitView，一般activity只会set一次contentView
        if (mUnbinder != null) return;
        mUnbinder = ButterKnife.bind(this);
        onInitView(getWindow().getDecorView());
    }
}

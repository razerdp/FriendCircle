package com.razerdp.module.impl.main.ui;

import android.view.View;

import com.razerdp.github.uilib.base.BaseLibActivity;

import static com.razerdp.module.impl.main.ui.MainActivity.Data;


public class MainActivity extends BaseLibActivity<Data> {

    @Override
    public int layoutId() {
        return 0;
    }

    @Override
    protected void onInitView(View rootView) {

    }

    public static class Data extends IntentData {
    }
}

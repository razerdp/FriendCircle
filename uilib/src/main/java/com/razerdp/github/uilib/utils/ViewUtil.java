package com.razerdp.github.uilib.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razerdp.github.lib.api.AppContext;

import androidx.annotation.LayoutRes;

/**
 * Created by 大灯泡 on 2019/8/2.
 */
public class ViewUtil {

    public static View inflate(@LayoutRes int layout, ViewGroup parent, boolean attachToParent) {
        return LayoutInflater.from(AppContext.getAppContext()).inflate(layout, parent, attachToParent);
    }
}

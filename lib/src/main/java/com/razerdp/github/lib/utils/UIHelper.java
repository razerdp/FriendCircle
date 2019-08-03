package com.razerdp.github.lib.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.Toast;

import com.razerdp.github.lib.api.AppContext;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * Created by 大灯泡 on 2019/8/3.
 */
public class UIHelper {

    public static int getColor(@ColorRes int colorResId) {
        try {
            return ContextCompat.getColor(AppContext.getAppContext(), colorResId);
        } catch (Exception e) {
            return Color.TRANSPARENT;
        }
    }


    public static void toast(@StringRes int textResId) {
        toast(StringUtil.getString(textResId));
    }

    public static void toast(String text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    public static void toast(@StringRes int textResId, int duration) {
        toast(StringUtil.getString(textResId), duration);
    }

    public static void toast(String text, int duration) {
        Toast.makeText(AppContext.getAppContext(), text, duration).show();
    }

    public static int getNavigationBarHeight() {
        Resources resources = AppContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            //获取NavigationBar的高度
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, Resources.getSystem().getDisplayMetrics());
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}

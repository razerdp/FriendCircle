package com.razerdp.github.lib.utils;

import android.text.TextUtils;

import com.razerdp.github.lib.api.AppContext;

import androidx.annotation.ArrayRes;
import androidx.annotation.StringRes;


/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * 字符串工具类
 */

public class StringUtil {

    public static boolean noEmpty(String originStr) {
        return !TextUtils.isEmpty(originStr);
    }


    public static boolean noEmpty(String... originStr) {
        boolean noEmpty = true;
        for (String s : originStr) {
            if (TextUtils.isEmpty(s)) {
                noEmpty = false;
                break;
            }
        }
        return noEmpty;
    }

    /**
     * 从资源文件拿到文字
     */
    public static String getString(@StringRes int strId, Object... objs) {
        if (strId == 0) return null;
        return AppContext.getAppContext().getResources().getString(strId, objs);
    }

    public static String[] getStringArray(@ArrayRes int strId) {
        if (strId == 0) return null;
        return AppContext.getAppContext().getResources().getStringArray(strId);
    }
}

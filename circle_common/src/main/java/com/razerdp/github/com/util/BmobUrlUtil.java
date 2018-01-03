package com.razerdp.github.com.util;

import android.support.annotation.IntRange;

import java.util.Locale;

import razerdp.github.com.lib.utils.StringUtil;

/**
 * Created by 大灯泡 on 2018/1/3.
 * <p>
 * bmob文件链接工具类
 */
public class BmobUrlUtil {

    private static final String thumbImage = "%s!/fxfn/%sx%s";
    private static final String scaleThumbImage = "%s!/scale/%s";//[1-1000]

    public static String getThumbImageUrl(String url, int width, int height) {
        if (StringUtil.noEmpty(url) && (url.endsWith(".jpg") || url.endsWith(".png"))) {
            return String.format(Locale.getDefault(), thumbImage, url, String.valueOf(width), String.valueOf(height));
        }
        return url;
    }

    public static String getThumbImageUrl(String url, @IntRange(from = 1, to = 1000) int scale) {
        if (StringUtil.noEmpty(url) && (url.endsWith(".jpg") || url.endsWith(".png"))) {
            return String.format(Locale.getDefault(), scaleThumbImage, url, scale);
        }
        return url;
    }

}

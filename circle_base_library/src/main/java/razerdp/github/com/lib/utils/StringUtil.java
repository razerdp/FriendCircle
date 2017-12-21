package razerdp.github.com.lib.utils;

import android.text.TextUtils;

import java.util.Locale;

import razerdp.github.com.lib.api.AppContext;

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
    public static String getResourceString(int strId) {
        String result = "";
        if (strId > 0) {
            result = AppContext.getResources().getString(strId);
        }
        return result;
    }

    /**
     * 从资源文件得到文字并format
     */
    public static String getResourceStringAndFormat(int strId, Object... objs) {
        String result = "";
        if (strId > 0) {
            result = String.format(Locale.getDefault(), AppContext.getResources().getString(strId), objs);
        }
        return result;
    }
}

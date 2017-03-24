package razerdp.github.com.baselibrary.utils;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.util.Locale;

import razerdp.github.com.baselibrary.base.AppContext;

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

    /**
     * 关键字高亮 （不支持模糊搜索）
     *
     * @param keyWord      关键字
     * @param color        高亮颜色
     * @param sourceString 完整的源文本
     * @return 拼接后的文字（CharSequence）
     */
    public static SpannableStringBuilder highLightKeyWord(String keyWord, int color,
                                                          String sourceString) {
        SpannableStringBuilder spanBuilder = null;
        if (!TextUtils.isEmpty(sourceString) && !TextUtils.isEmpty(keyWord) && sourceString.contains(keyWord)) {
            spanBuilder = new SpannableStringBuilder(sourceString);
            int index = sourceString.indexOf(keyWord);
            if (index >= 0) {
                int sourceStrLen = sourceString.length();
                // 遍历关键字，当关键字没有的时候,跳出循环
                while (index < sourceStrLen && index >= 0) {
                    spanBuilder.setSpan(new ForegroundColorSpan(color), index, index + keyWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanBuilder.setSpan(new StyleSpan(Typeface.NORMAL), index, index + keyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    index = sourceString.indexOf(keyWord, index + keyWord.length());
                }
            }
        } else {
            if (TextUtils.isEmpty(sourceString)) {
                spanBuilder = new SpannableStringBuilder("");
            } else {
                spanBuilder = new SpannableStringBuilder(sourceString);
            }
        }

        return spanBuilder;
    }

    /**
     * 关键字高亮，其他字规定颜色 (因为spannablestringbuilder会以黑色默认)【不支持模糊搜索】
     *
     * @param defaultColor 其他字的颜色
     */
    public static SpannableStringBuilder highLightKeyWord(String keyWord, int defaultColor, int color,
                                                          String sourceString) {
        SpannableStringBuilder spanBuilder = null;
        if (!TextUtils.isEmpty(sourceString) && !TextUtils.isEmpty(keyWord) && sourceString.contains(keyWord)) {
            spanBuilder = new SpannableStringBuilder(sourceString);
            spanBuilder.setSpan(new ForegroundColorSpan(defaultColor), 0, sourceString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int index = sourceString.indexOf(keyWord);
            if (index >= 0) {
                int sourceStrLen = sourceString.length();
                // 遍历关键字，当关键字没有的时候,跳出循环
                while (index < sourceStrLen && index >= 0) {
                    spanBuilder.setSpan(new ForegroundColorSpan(color), index, index + keyWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanBuilder.setSpan(new StyleSpan(Typeface.NORMAL), index, index + keyWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    index = sourceString.indexOf(keyWord, index + keyWord.length());
                }
            }
        } else {
            if (TextUtils.isEmpty(sourceString)) {
                spanBuilder = new SpannableStringBuilder("");
            } else {
                spanBuilder = new SpannableStringBuilder(sourceString);
                spanBuilder.setSpan(new ForegroundColorSpan(defaultColor), 0, sourceString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spanBuilder;
    }
}

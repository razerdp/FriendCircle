package razerdp.github.com.baselibrary.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import java.util.List;

import razerdp.github.com.baselibrary.R;
import razerdp.github.com.baselibrary.base.AppContext;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;

/**
 * Created by 大灯泡 on 2016/10/27.
 */

public class ToolUtil {

    public static boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    //复制到剪切板
    @SuppressLint({"NewApi", "ServiceCast"})
    public static void copyToClipboard(String szContent) {
        Context context = AppContext.getAppContext();

        String sourceText = szContent;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(sourceText);
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", sourceText);
            clipboard.setPrimaryClip(clip);
        }
    }

    //复制到剪切板
    @SuppressLint({"NewApi", "ServiceCast"})
    public static void copyToClipboardAndToast(Context context, String szContent) {

        String sourceText = szContent;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(sourceText);
            UIHelper.ToastMessage("复制成功");
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", sourceText);
            clipboard.setPrimaryClip(clip);
            UIHelper.ToastMessage("复制成功");
        }
    }
}

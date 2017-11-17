package razerdp.github.com.lib.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import java.util.List;

import razerdp.github.com.lib.api.AppContext;

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
            Toast.makeText(context,"copy success",Toast.LENGTH_SHORT).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", sourceText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context,"copy success",Toast.LENGTH_SHORT).show();
        }
    }
}

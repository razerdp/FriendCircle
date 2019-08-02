package com.razerdp.github.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import androidx.core.content.FileProvider;

/**
 * Created by 大灯泡 on 2016/10/27.
 */

public class ToolUtil {

    public static boolean isEmpty(Collection<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }


    public static boolean indexInCollection(Collection who, int index) {
        if (who == null) return false;
        return index >= 0 && index < who.size();
    }


    public static void install(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            File file = (new File(apkPath));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkUri = FileProvider.getUriForFile(context, "github.razerdp.friendcircle", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}

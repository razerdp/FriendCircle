package com.razerdp.github.uilib.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

/**
 * Created by 大灯泡 on 2019/8/2.
 * <p>
 * 打开界面必须通过该launcher打开
 */
public class ActivityLauncher {

    public static <D extends BaseLibActivity.IntentData> void start(Object from, Class<? extends Activity> target, D data) {
        if (!(from instanceof Context) && !(from instanceof Fragment)) return;
        boolean hasRequest = data != null && data.getRequestCode() != -1;

        Intent intent;

        if (from instanceof Context) {
            intent = new Intent((Context) from, target);
        } else {
            intent = new Intent(((Fragment) from).getContext(), target);
        }

        if (data != null) {
            data.writeToIntent(intent);
        }

        if (from instanceof Activity) {
            if (hasRequest) {
                ((Activity) from).startActivityForResult(intent, data.getRequestCode());
            } else {
                ((Activity) from).startActivity(intent);
            }
            return;
        }

        if (from instanceof Fragment) {
            if (hasRequest) {
                ((Fragment) from).startActivityForResult(intent, data.getRequestCode());
            } else {
                ((Fragment) from).startActivity(intent);
            }
            return;
        }

        ((Context) from).startActivity(intent);
    }
}

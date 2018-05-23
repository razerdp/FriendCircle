package razerdp.github.com.lib.helper;

import android.support.annotation.NonNull;

import java.util.List;

import razerdp.github.com.lib.interfaces.OnPermissionGrantListener;

/**
 * Created by 大灯泡 on 2018/5/7.
 * <p>
 * 针对权限与不同厂商适配
 * <p>
 * 本类所有返回boolean的方法都有如下规定：
 * <p>
 * 当返回true，外部{@link PermissionHelper}的对应方法才能继续执行
 * 当返回false，外部{@link PermissionHelper}的对应方法中断
 */
class PermissionHelperCompat {


    void onRequestPermission(OnPermissionGrantListener listener, PermissionHelper.Permission... permissions) {

    }

    void onHandlePermissionResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    boolean onDontAskPermission(List<String> dontAskPermission) {

        return true;
    }

    boolean onBeforeCallGrantResult(PermissionHelper.Permission... permissions) {

        return true;
    }

    boolean onBeforeCallDeniedResult(PermissionHelper.Permission... permissions) {

        return true;
    }
}

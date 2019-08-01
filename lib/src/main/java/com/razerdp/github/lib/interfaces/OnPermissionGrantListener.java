package com.razerdp.github.lib.interfaces;


import com.razerdp.github.lib.helper.PermissionHelper;

import java.util.Collection;

/**
 * Created by 大灯泡 on 2018/5/7.
 */
public interface OnPermissionGrantListener {
    void onPermissionGranted(Collection<PermissionHelper.Permission> permissions);

    void onPermissionsDenied(Collection<PermissionHelper.Permission> permissions);

    abstract class OnPermissionGrantListenerAdapter implements OnPermissionGrantListener {
        @Override
        public void onPermissionGranted(Collection<PermissionHelper.Permission> permissions) {

        }

        @Override
        public void onPermissionsDenied(Collection<PermissionHelper.Permission> permissions) {

        }
    }
}

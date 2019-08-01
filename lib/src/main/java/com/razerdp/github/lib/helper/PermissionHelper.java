package com.razerdp.github.lib.helper;

import android.Manifest;
import android.text.TextUtils;

import com.razerdp.github.lib.api.AppContext;
import com.razerdp.github.lib.fragment.PermissionFragment;
import com.razerdp.github.lib.interfaces.OnPermissionGrantListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.fragment.app.FragmentActivity;


/**
 * 权限帮助类
 */

public class PermissionHelper {
    private static final String TAG = "PermissionHelper";
    private static final int REQUEST_CODE_REQUEST_PERMISSION = 6666;

    private Set<Permission> mRequestPermissions;
    private FragmentActivity mActivity;
    private OnPermissionGrantListener mOnPermissionGrantListener;


    public enum Permission {
        RECORD_AUDIO(Manifest.permission.RECORD_AUDIO, "录音"),
        GET_ACCOUNTS(Manifest.permission.GET_ACCOUNTS, "访问账户Gmail列表"),
        READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE, "读取电话状态"),
        CALL_PHONE(Manifest.permission.CALL_PHONE, "拨打电话"),
        CAMERA(Manifest.permission.CAMERA, "拍照权限"),
        ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION, "获取精确位置"),
        ACCESS_COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION, "获取粗略位置"),
        READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE, "读外部存储"),
        WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写外部存储");

        public final String permission;
        public String desc;

        Permission(String permission, String desc) {
            this.permission = permission;
            this.desc = desc;
        }

        public String getTips() {
            return desc;
        }

        public Permission setTips(String tips) {
            this.desc = tips;
            return this;
        }

        public static Collection<Permission> permissionsStrToPermission(String[] permission) {
            if (permission.length == 0 || permission == null) return null;
            List<Permission> permissions = new ArrayList<>();
            for (String s : permission) {
                for (Permission permission1 : Permission.values()) {
                    if (TextUtils.equals(permission1.permission, s)) {
                        permissions.add(permission1);
                    }
                }
            }
            return permissions;
        }
    }

    private PermissionHelper(FragmentActivity activity) {
        mRequestPermissions = new HashSet<>();
        LifeCycleHelper.handle(this, activity, new LifeCycleHelper.CallbackAdapter<PermissionHelper>() {
            @Override
            public void onDestroy(PermissionHelper obj) {
                destroy();
            }
        });
    }

    private void destroy() {
        mActivity = null;
        mOnPermissionGrantListener = null;
    }

    public static PermissionHelper get() throws NullPointerException {
        FragmentActivity top = AppContext.getTopActivity();
        if (top == null) {
            throw new NullPointerException("topActivity为空，无法继续执行");
        }
        return with(top);
    }

    public static PermissionHelper with(FragmentActivity activity) {
        return new PermissionHelper(activity);
    }


    public PermissionHelper addPermission(Permission permission) {
        mRequestPermissions.add(permission);
        return this;
    }

    public PermissionHelper listener(OnPermissionGrantListener listener) {
        this.mOnPermissionGrantListener = listener;
        return this;
    }

    /**
     * 请求权限
     */
    public final void request() {
        PermissionFragment fragment = FragmentInjectHelper.inject(mActivity, PermissionFragment.class);
        fragment.setPermissions(mRequestPermissions)
                .setListener(mOnPermissionGrantListener);
    }

}

package com.razerdp.github.lib.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.razerdp.github.lib.interfaces.OnPermissionGrantListener;
import com.razerdp.github.lib.utils.KLog;
import com.razerdp.github.lib.utils.ToolUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static com.razerdp.github.lib.helper.PermissionHelper.Permission;

/**
 * Created by 大灯泡 on 2019/8/1.
 * <p>
 * 权限请求的fragment
 */
public class PermissionFragment extends Fragment {

    private Set<Permission> mRequestPermissions;
    private OnPermissionGrantListener mOnPermissionGrantListener;
    private static final int REQUEST_CODE = 666;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public PermissionFragment setPermissions(Set<Permission> permissions) {
        if (permissions == null) return this;
        if (mRequestPermissions == null) {
            mRequestPermissions = new HashSet<>(permissions);
        } else {
            mRequestPermissions.clear();
            mRequestPermissions.addAll(permissions);
        }
        return this;
    }

    public PermissionFragment setListener(OnPermissionGrantListener onPermissionGrantListener) {
        mOnPermissionGrantListener = onPermissionGrantListener;
        return this;
    }


    //=============================================================

    public void requestPermission() {
        if (!needDoNext(mRequestPermissions)) {
            return;
        }
        //保存相关信息
        //检查是否已经拥有权限
        List<String> noGrantedPermission = checkHasGrantedAndRetrunNoGranted(mRequestPermissions);
        if (ToolUtil.isListEmpty(noGrantedPermission)) {
            callPermissionGranted(mRequestPermissions);
            return;
        }

        KLog.i("申请权限：\n", noGrantedPermission);
        //针对没拥有的权限进行请求权限
        requestPermissions(noGrantedPermission.toArray(new String[noGrantedPermission.size()]), REQUEST_CODE);
    }

    private static void shoMessageDialog(final Context context, String title, String message, String buttonText, final DialogInterface.OnClickListener
            dialogButtonClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, dialogButtonClickListener)
                .create()
                .show();
    }

    public void handlePermissionsResult(final int requestCode, @NonNull String[] permissions,
                                        @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                int result = grantResults[i];
                if (result != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (deniedPermissions.size() <= 0) {
                callPermissionGranted(Permission.permissionsStrToPermission(permissions));
                return;
            }
            KLog.i("权限被拒绝：\n", deniedPermissions);
            callPermissionDenied(Permission.permissionsStrToPermission(deniedPermissions.toArray(new String[deniedPermissions.size()])));
            //是否勾选不再询问
            List<String> dontAskPermission = new ArrayList<>();
            for (String deniedPermission : deniedPermissions) {
                if (!shouldShowRequestPermissionRationale(deniedPermission)) {
                    dontAskPermission.add(deniedPermission);
                }
            }
            KLog.i("权限被永久拒绝：\n", dontAskPermission);
            if (dontAskPermission.size() > 0) {
                openSettingActivity(getContext(), Permission.permissionsStrToPermission(dontAskPermission.toArray(new String[dontAskPermission.size()])));
            }

        }

    }

    private static void openSettingActivity(final Context context, Collection<Permission> permissions) {
        StringBuffer message = new StringBuffer();
        if (permissions != null) {
            for (Permission permission : permissions) {
                message.append(permission.desc)
                        .append("\n");
            }
        }
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle("授权被禁止，需要手动授权")
                .setMessage(message)
                .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        dialog.show();
    }


    private void callPermissionGranted(Collection<Permission> permissions) {
        if (mOnPermissionGrantListener != null) {
            mOnPermissionGrantListener.onPermissionGranted(permissions);
        }
    }

    private void callPermissionDenied(Collection<Permission> permissions) {
        if (mOnPermissionGrantListener != null) {
            mOnPermissionGrantListener.onPermissionsDenied(permissions);
        }
    }

    public OnPermissionGrantListener getOnPermissionGrantListener() {
        return mOnPermissionGrantListener;
    }

    public void setOnPermissionGrantListener(OnPermissionGrantListener onPermissionGrantListener) {
        mOnPermissionGrantListener = onPermissionGrantListener;
    }

    //-----------------------------------------tools-----------------------------------------

    private boolean needDoNext(Collection<Permission> permissions) {
        //低于23的直接返回
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callPermissionGranted(permissions);
            return false;
        }

        //预检查
        if (permissions == null || permissions.size() <= 0) {
            callPermissionGranted(permissions);
            return false;
        }
        return true;
    }

    /**
     * 检查是否拥有权限
     *
     * @return 没授权的权限
     */
    private List<String> checkHasGrantedAndRetrunNoGranted(Collection<Permission> permissions) {
        List<String> result = new ArrayList<>();
        for (Permission permissionWrap : permissions) {
            try {
                if (ContextCompat.checkSelfPermission(getContext(), permissionWrap.permission) != PackageManager.PERMISSION_GRANTED) {
                    result.add(permissionWrap.permission);
                } else {
                    KLog.i("已经拥有权限：\n", permissionWrap);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                result.add(permissionWrap.permission);
            }
        }
        return result;
    }
}

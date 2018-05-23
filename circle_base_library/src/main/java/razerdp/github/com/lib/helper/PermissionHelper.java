package razerdp.github.com.lib.helper;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.socks.library.KLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.lib.interfaces.OnPermissionGrantListener;
import razerdp.github.com.lib.utils.ToolUtil;


/**
 * 权限帮助类
 */

public class PermissionHelper {
    private static final String TAG = "PermissionHelper";
    private WeakReference<Object> mWeakReference;
    private OnPermissionGrantListener mOnPermissionGrantListener;
    private PermissionHelperCompat mHelperCompat;

    private static final int REQUEST_CODE_REQUEST_PERMISSION = 6666;

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

        private final String permission;
        private String desc;

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

        public static Permission[] permissionsStrToPermission(String[] permission) {
            if (permission.length == 0 || permission == null) return null;
            List<Permission> permissions = new ArrayList<>();
            for (String s : permission) {
                for (Permission permission1 : Permission.values()) {
                    if (TextUtils.equals(permission1.permission, s)) {
                        permissions.add(permission1);
                    }
                }
            }
            return permissions.toArray(new Permission[permissions.size()]);
        }
    }

    public PermissionHelper(Activity act) {
        mWeakReference = new WeakReference<Object>(act);
        mHelperCompat = new PermissionHelperCompat();
    }

    public PermissionHelper(Fragment frag) {
        mWeakReference = new WeakReference<Object>(frag);
        mHelperCompat = new PermissionHelperCompat();
    }

    public void requestPermission(OnPermissionGrantListener listener, Permission... permissions) {
        setOnPermissionGrantListener(listener);
        if (!needDoNext(permissions)) return;
        mHelperCompat.onRequestPermission(listener, permissions);
        //保存相关信息
        //检查是否已经拥有权限
        List<String> noGrantedPermission = checkHasGrantedAndRetrunNoGranted(permissions);
        if (ToolUtil.isListEmpty(noGrantedPermission)) {
            callPermissionGranted(permissions);
            return;
        }

        log("申请权限：\n", noGrantedPermission);
        //针对没拥有的权限进行请求权限
        requestPermissionsInternal(noGrantedPermission.toArray(new String[noGrantedPermission.size()]), REQUEST_CODE_REQUEST_PERMISSION);
    }

    private void requestPermissionsInternal(final @NonNull String[] permissions, final @IntRange(from = 0) int requestCode) {
        if (getActivity() != null) {
            ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
        } else if (getFragment() != null) {
            getFragment().requestPermissions(permissions, requestCode);
        }
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
        if (!checkTargetValided()) return;
        if (requestCode == REQUEST_CODE_REQUEST_PERMISSION) {
            mHelperCompat.onHandlePermissionResult(requestCode, permissions, grantResults);
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
            log("权限被拒绝：\n", deniedPermissions);
            callPermissionDenied(Permission.permissionsStrToPermission(deniedPermissions.toArray(new String[deniedPermissions.size()])));
            //是否勾选不再询问
            List<String> dontAskPermission = new ArrayList<>();
            for (String deniedPermission : deniedPermissions) {
                if (!shouldShowRequestPermissionRationale(deniedPermission)) {
                    dontAskPermission.add(deniedPermission);
                }
            }
            log("权限被永久拒绝：\n", dontAskPermission);
            boolean doNext = mHelperCompat.onDontAskPermission(dontAskPermission);
            if (!doNext) return;
            if (dontAskPermission.size() > 0) {
                openSettingActivity(getContext(), Permission.permissionsStrToPermission(dontAskPermission.toArray(new String[dontAskPermission.size()])));
            }

        }

    }

    private static void openSettingActivity(final Context context, Permission... permissions) {
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

    public Context getContext() {
        Activity activity = getActivity();
        Fragment fragment = getFragment();
        if (activity == null && fragment == null) {
            return null;
        }
        if (activity == null) {
            return fragment.getContext();
        }
        if (fragment == null) {
            return activity;
        }
        return null;
    }

    private Fragment getFragment() {
        Object object = mWeakReference == null ? null : mWeakReference.get();
        return object instanceof Fragment ? ((Fragment) object) : null;
    }

    private Activity getActivity() {
        Object object = mWeakReference == null ? null : mWeakReference.get();
        return object instanceof Activity ? ((Activity) object) : null;
    }


    private void callPermissionGranted(Permission... permissions) {
        boolean canCall = mHelperCompat.onBeforeCallGrantResult(permissions);
        if (mOnPermissionGrantListener != null) {
            if (canCall) {
                mOnPermissionGrantListener.onPermissionGranted(permissions);
            }
        }
    }

    private void callPermissionDenied(Permission... permissions) {
        boolean canCall = mHelperCompat.onBeforeCallDeniedResult(permissions);
        if (mOnPermissionGrantListener != null) {
            if (canCall) {
                mOnPermissionGrantListener.onPermissionsDenied(permissions);
            }
        }
    }

    public void handleDestroy() {
        if (mWeakReference != null) {
            mWeakReference.clear();
        }
        mOnPermissionGrantListener = null;
        mWeakReference = null;
    }

    public OnPermissionGrantListener getOnPermissionGrantListener() {
        return mOnPermissionGrantListener;
    }

    public void setOnPermissionGrantListener(OnPermissionGrantListener onPermissionGrantListener) {
        mOnPermissionGrantListener = onPermissionGrantListener;
    }

    //-----------------------------------------tools-----------------------------------------

    private boolean needDoNext(Permission[] permissions) {
        //低于23的直接返回
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callPermissionGranted(permissions);
            return false;
        }

        //预检查
        if (!checkTargetValided()) return false;
        if (permissions == null || permissions.length <= 0) {
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
    private List<String> checkHasGrantedAndRetrunNoGranted(Permission... permissions) {
        List<String> result = new ArrayList<>();
        for (Permission permissionWrap : permissions) {
            try {
                if (ContextCompat.checkSelfPermission(getContext(), permissionWrap.permission) != PackageManager.PERMISSION_GRANTED) {
                    result.add(permissionWrap.permission);
                } else {
                    log("已经拥有权限：\n", permissionWrap);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                result.add(permissionWrap.permission);
            }
        }
        return result;
    }

    private boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        if (getActivity() != null) {
            return ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
        } else if (getFragment() != null) {
            return getFragment().shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    //检查是否符合activity或者fragment
    private boolean checkTargetValided() {
        return mWeakReference != null && mWeakReference.get() != null && (mWeakReference.get() instanceof Activity || mWeakReference.get() instanceof Fragment);
    }

    private void log(String desc, List<String> data) {
        String logText = null;
        if (!ToolUtil.isListEmpty(data)) {
            for (String datum : data) {
                logText = datum + "\n";
            }
        }
        KLog.i(TAG, desc + logText);
    }

    private void log(String desc, Permission... permissions) {
        String logText = null;
        if (permissions != null) {
            for (Permission permission : permissions) {
                logText = permission.permission + "(" + permission.desc + ")" + "\n";
            }
        }
        KLog.i(TAG, desc + logText);
    }
}

package razerdp.github.com.lib.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import razerdp.github.com.lib.R;


/**
 * 权限帮助类
 */

public class PermissionHelper {
    private static final String TAG = "PermissionHelper";
    private WeakReference<Object> mWeakReference;
    private OnPermissionGrantListener mOnPermissionGrantListener;

    private static final HashMap<Integer, String> PERMISSION_MAP = new HashMap<>();


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CODE_RECORD_AUDIO, CODE_GET_ACCOUNTS, CODE_READ_PHONE_STATE, CODE_CALL_PHONE, CODE_CAMERA, CODE_ACCESS_FINE_LOCATION,
            CODE_ACCESS_COARSE_LOCATION, CODE_READ_EXTERNAL_STORAGE, CODE_WRITE_EXTERNAL_STORAGE})
    public @interface PermissionResultCode {
    }

    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_GET_ACCOUNTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CALL_PHONE = 3;
    public static final int CODE_CAMERA = 4;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 8;
    public static final int CODE_MULTI_PERMISSION = 100;


    static {
        PERMISSION_MAP.put(CODE_RECORD_AUDIO, Manifest.permission.RECORD_AUDIO);
        PERMISSION_MAP.put(CODE_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
        PERMISSION_MAP.put(CODE_READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);
        PERMISSION_MAP.put(CODE_CALL_PHONE, Manifest.permission.CALL_PHONE);
        PERMISSION_MAP.put(CODE_CAMERA, Manifest.permission.CAMERA);
        PERMISSION_MAP.put(CODE_ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        PERMISSION_MAP.put(CODE_ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        PERMISSION_MAP.put(CODE_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        PERMISSION_MAP.put(CODE_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static String getPermissionString(@PermissionResultCode int code) {
        return PERMISSION_MAP.get(code);
    }

    public PermissionHelper(Activity activity) {
        mWeakReference = new WeakReference<Object>(activity);
    }

    public PermissionHelper(Fragment fragment) {
        mWeakReference = new WeakReference<Object>(fragment);
    }

    public void requestPermission(@PermissionResultCode final int requestCode, OnPermissionGrantListener listener) {
        setOnPermissionGrantListener(listener);
        //低于23的直接返回
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callPermissionGranted(requestCode);
            return;
        }

        if (getContext() == null) {
            return;
        }

        if (requestCode < 0 || !PERMISSION_MAP.containsKey(requestCode)) {
            Log.e(TAG, "非法rquestCode，请传入@PermissionResultCode里面的值");
            callPermissionDenied(requestCode);
            return;
        }

        final String requestPermission = PERMISSION_MAP.get(requestCode);
        int checkSelfPermission;
        try {
            checkSelfPermission = ContextCompat.checkSelfPermission(getContext(), requestPermission);
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage());
            openSettingActivity(getContext(), "需要手动授权以下权限");
            return;
        }

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(requestPermission)) {
                shouldShowRationale(getContext(), requestCode, requestPermission);
            } else {
                requestPermissionsInternal(new String[]{requestPermission}, requestCode);
            }
        } else {
            callPermissionGranted(requestCode);
        }
    }

    private void requestMultiResult(String[] permissions, int[] grantResults, OnPermissionGrantListener onPermissionGrantListener) {
        setOnPermissionGrantListener(onPermissionGrantListener);
        //低于23的直接返回
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callPermissionGranted(CODE_MULTI_PERMISSION);
            return;
        }

        if (getContext() == null) {
            return;
        }

        Map<String, Integer> perms = new HashMap<>();
        ArrayList<String> notGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            perms.put(permissions[i], grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permissions[i]);
            }
        }

        if (notGranted.size() == 0) {
            callPermissionGranted(CODE_MULTI_PERMISSION);
        } else {
            openSettingActivity(getContext(), "需要手动授权权限");
        }
    }

    /**
     * 自行申请没有的权限
     */
    public void autoRequestPermissions(OnPermissionGrantListener listener) {
        setOnPermissionGrantListener(listener);
        if (getContext() == null) {
            callPermissionDenied(CODE_MULTI_PERMISSION);
            return;
        }
        final List<String> permissionsList = getNoGrantedPermission(getContext(), false);
        final List<String> shouldRationalePermissionsList = getNoGrantedPermission(getContext(), true);

        if (permissionsList == null || shouldRationalePermissionsList == null) {
            return;
        }

        if (permissionsList.size() > 0) {
            requestPermissionsInternal(permissionsList.toArray(new String[permissionsList.size()]), CODE_MULTI_PERMISSION);

        } else if (shouldRationalePermissionsList.size() > 0) {
            shoMessageDialog(getContext(), "需要权限", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissionsInternal(shouldRationalePermissionsList.toArray(new String[shouldRationalePermissionsList.size()]),
                            CODE_MULTI_PERMISSION);
                }
            });
        } else {
            callPermissionGranted(CODE_MULTI_PERMISSION);
        }
    }

    private void requestPermissions(final @NonNull String[] permissions, final @IntRange(from = 0) int requestCode) {
        Object object = mWeakReference == null ? null : mWeakReference.get();
        if (object == null) return;
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions(((Activity) object), permissions, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(permissions, requestCode);
        }

    }


    private void shouldShowRationale(final Context activity, final int requestCode, final String requestPermission) {
        String[] permissionsHint = activity.getResources().getStringArray(R.array.permissions);
        shoMessageDialog(activity, "Rationale: " + permissionsHint[requestCode], "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissionsInternal(new String[]{requestPermission}, requestCode);
            }
        });
    }

    private static void shoMessageDialog(final Context context, String message, String buttonText, final DialogInterface.OnClickListener
            dialogButtonClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(message)
                .setPositiveButton(buttonText, dialogButtonClickListener)
                .create()
                .show();
    }

    public void handlePermissionsResult(final int requestCode, @NonNull String[] permissions,
                                        @NonNull int[] grantResults) {

        if (getContext() == null) {
            return;
        }
        if (requestCode == CODE_MULTI_PERMISSION) {
            requestMultiResult(permissions, grantResults, mOnPermissionGrantListener);
            return;
        }

        if (requestCode < 0 || !PERMISSION_MAP.containsKey(requestCode)) {
            Log.e(TAG, "非法rquestCode，请传入@PermissionResultCode里面的值");
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callPermissionGranted(requestCode);
        } else {
            String[] permissionsHint = getContext().getResources().getStringArray(R.array.permissions);
            openSettingActivity(getContext(), "Result" + permissionsHint[requestCode]);
        }

    }

    private static void openSettingActivity(final Context context, String message) {

        shoMessageDialog(context, "手动授权", message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
    }


    private ArrayList<String> getNoGrantedPermission(Context activity, boolean isShouldRationale) {

        ArrayList<String> permissions = new ArrayList<>();

        Iterator iterator = PERMISSION_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<Integer, String> entry = (HashMap.Entry<Integer, String>) iterator.next();
            String requestPermission = entry.getValue();
            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ContextCompat.checkSelfPermission(activity, requestPermission);
            } catch (RuntimeException e) {
                Log.e(TAG, "RuntimeException:" + e.getMessage());
                return null;
            }

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(requestPermission)) {
                    if (isShouldRationale) {
                        permissions.add(requestPermission);
                    }
                } else {
                    if (!isShouldRationale) {
                        permissions.add(requestPermission);
                    }
                }
            }
        }
        return permissions;
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

    private void requestPermissionsInternal(final @NonNull String[] permissions, final @IntRange(from = 0) int requestCode) {
        if (getActivity() != null) {
            ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
        } else if (getFragment() != null) {
            getFragment().requestPermissions(permissions, requestCode);
        }
    }

    private boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        if (getActivity() != null) {
            return ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
        } else if (getFragment() != null) {
            return getFragment().shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    private void callPermissionGranted(int requestCode) {
        if (mOnPermissionGrantListener != null) {
            mOnPermissionGrantListener.onPermissionGranted(requestCode);
        }
    }

    private void callPermissionDenied(int requestCode) {
        if (mOnPermissionGrantListener != null) {
            mOnPermissionGrantListener.onPermissionsDenied(requestCode);
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

    public interface OnPermissionGrantListener {
        void onPermissionGranted(@PermissionResultCode int requestCode);

        void onPermissionsDenied(@PermissionResultCode int requestCode);
    }

}

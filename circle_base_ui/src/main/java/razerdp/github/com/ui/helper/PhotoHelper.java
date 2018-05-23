package razerdp.github.com.ui.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import razerdp.github.com.lib.helper.AppFileHelper;
import razerdp.github.com.lib.helper.PermissionHelper;
import razerdp.github.com.lib.interfaces.IPermission;
import razerdp.github.com.lib.interfaces.OnPermissionGrantListener;
import razerdp.github.com.lib.utils.ImageSelectUtil;

/**
 * Created by 大灯泡 on 2017/4/10.
 * <p>
 * 图片拍照和选择
 * <p>
 * 务必注意fragment和activity，因为fragment调用getActivity不会调用到onActivityResult
 */

public class PhotoHelper {
    private static String curTempPhotoPath;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REQUEST_FROM_CAMERA, REQUEST_FROM_ALBUM, REQUEST_FROM_CROP})
    public @interface PhotoType {
    }

    public static final int REQUEST_FROM_CAMERA = 0x10;
    public static final int REQUEST_FROM_ALBUM = 0x11;
    public static final int REQUEST_FROM_CROP = 0x12;
    private static final int REQUEST_FROM_CAMERA_WITHO_OUT_CROP = 0x13;//不截图的request code
    private static final int REQUEST_FROM_ALBUM_WITHO_OUT_CROP = 0x14;//不截图的request code


    private static String cropPath;


    public static void fromCamera(Object obj) {
        fromCamera(obj, true);
    }

    public static void fromCamera(Object obj, boolean needCrop) {
        if (!checkTargetValided(obj)) return;
        checkAndRequestCameraPermission(obj, needCrop, false);
    }

    public static void fromAlbum(Object obj) {
        fromAlbum(obj, true);
    }

    public static void fromAlbum(Object obj, boolean needCrop) {
        if (!checkTargetValided(obj)) return;
        checkAndRequestCameraPermission(obj, needCrop, true);
    }

    public static String getCropImagePath() {
        return AppFileHelper.getAppTempPath() + AppFileHelper.createCropImageName();
    }

    public static void toCrop(Object obj, Uri imageUri) {
        if (!checkTargetValided(obj)) return;
        toCropInternal(obj, imageUri);
    }

    private static void checkAndRequestCameraPermission(final Object object, final boolean needCrop, final boolean album) {
        PermissionHelper permissionHelper = null;
        OnPermissionGrantListener onPermissionGrantListener = new OnPermissionGrantListener() {
            @Override
            public void onPermissionGranted(PermissionHelper.Permission... grantedPermissions) {
                if (album) {
                    fromAlbumInternal(object, needCrop);
                } else {
                    fromCameraInternal(object, needCrop);
                }
            }

            @Override
            public void onPermissionsDenied(PermissionHelper.Permission... deniedPermissions) {

            }
        };
        if (object instanceof Activity) {
            permissionHelper = object instanceof IPermission ? ((IPermission) object).getPermissionHelper() : new PermissionHelper((Activity) object);
        } else if (object instanceof Fragment) {
            permissionHelper = object instanceof IPermission ? ((IPermission) object).getPermissionHelper() : new PermissionHelper(((Fragment) object).getActivity());
        }
        if (permissionHelper != null) {
            if (album) {
                permissionHelper.requestPermission(onPermissionGrantListener, PermissionHelper.Permission.WRITE_EXTERNAL_STORAGE, PermissionHelper.Permission.READ_EXTERNAL_STORAGE);
            } else {
                permissionHelper.requestPermission(onPermissionGrantListener, PermissionHelper.Permission.CAMERA, PermissionHelper.Permission.WRITE_EXTERNAL_STORAGE, PermissionHelper.Permission.READ_EXTERNAL_STORAGE);
            }
        }

    }


    private static void fromAlbumInternal(Object obj, boolean needCrop) {
        if (obj == null) return;
        cropPath = getCropImagePath();
        // 跳转到系统相册
        Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        albumIntent.setType("image/*");
        if (obj instanceof Fragment) {
            ((Fragment) obj).startActivityForResult(albumIntent, needCrop ? REQUEST_FROM_ALBUM : REQUEST_FROM_ALBUM_WITHO_OUT_CROP);
        } else if (obj instanceof Activity) {
            ((Activity) obj).startActivityForResult(albumIntent, needCrop ? REQUEST_FROM_ALBUM : REQUEST_FROM_ALBUM_WITHO_OUT_CROP);
        }
    }

    private static void fromCameraInternal(Object object, boolean needCrop) {
        if (object == null) return;
        cropPath = getCropImagePath();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        curTempPhotoPath = AppFileHelper.getCameraPath() + AppFileHelper.createShareImageName();
        Uri uri = CompatHelper.getUri(new File(curTempPhotoPath));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (CompatHelper.isOverM()) {
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(cameraIntent, needCrop ? REQUEST_FROM_CAMERA : REQUEST_FROM_CAMERA_WITHO_OUT_CROP);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(cameraIntent, needCrop ? REQUEST_FROM_CAMERA : REQUEST_FROM_CAMERA_WITHO_OUT_CROP);
        }
    }

    private static void toCropInternal(Object obj, Uri imageUri) {
        if (obj == null || imageUri == null) return;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", imageCover.getWidth());// 输出图片大小
//        intent.putExtra("outputY", imageCover.getHeight());

        //此处使用uri.fromFile，而不要用fileProvider，因为非export，而后面自动授权，所以不会出现安全异常
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cropPath)));
        intent.putExtra("return-data", false);
        if (CompatHelper.isOverM()) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        if (obj instanceof Activity) {
            ((Activity) obj).startActivityForResult(intent, REQUEST_FROM_CROP);
        } else if (obj instanceof Fragment) {
            ((Fragment) obj).startActivityForResult(intent, REQUEST_FROM_CROP);
        }
    }


    public static void handleActivityResult(Activity act, int requestCode, int resultCode, Intent data, @Nullable PhotoCallback callback) {
        boolean needCrop = true;
        switch (requestCode) {
            case REQUEST_FROM_CAMERA_WITHO_OUT_CROP:
                needCrop = false;
            case REQUEST_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(curTempPhotoPath);
                    if (file.exists()) {
                        if (needCrop) {
                            toCrop(act, CompatHelper.getUri(file));
                        } else {
                            callFinish(callback, curTempPhotoPath);
                        }
                    } else {
                        callError(callback, "can not get the taked photo");
                    }
                }
                break;
            case REQUEST_FROM_ALBUM_WITHO_OUT_CROP:
                needCrop = false;
            case REQUEST_FROM_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        final Uri photoUri = data.getData();
                        if (photoUri == null) {
                            callError(callback, "加载图片失败");
                            return;
                        }
                        try {
                            if (CompatHelper.isOverN()) {
                                if (needCrop) {
                                    //7.0用的是content uri
                                    toCrop(act, photoUri);
                                } else {
                                    callFinish(callback, ImageSelectUtil.getPath(act, photoUri));
                                }
                            } else {
                                //非7.0用的file uri
                                String path = ImageSelectUtil.getPath(act, photoUri);
                                if (needCrop) {
                                    toCrop(act, CompatHelper.getUri(new File(path)));
                                } else {
                                    callFinish(callback, path);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callError(callback, "图片不存在");
                        }
                    } else {
                        callError(callback, "图片不存在");
                    }
                }
                break;
            case REQUEST_FROM_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        callError(callback, "图片不存在");
                        return;
                    }
                    callFinish(callback, cropPath);
                } else {
                    callError(callback, "取消");
                }
                break;

        }
    }

    public static void handleActivityResult(Fragment fragment, int requestCode, int resultCode, Intent data, @Nullable PhotoCallback callback) {
        boolean needCrop = true;
        switch (requestCode) {
            case REQUEST_FROM_CAMERA_WITHO_OUT_CROP:
                needCrop = false;
            case REQUEST_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(curTempPhotoPath);
                    if (file.exists()) {
                        if (needCrop) {
                            toCrop(fragment, CompatHelper.getUri(file));
                        } else {
                            callFinish(callback, curTempPhotoPath);
                        }
                    } else {
                        callError(callback, "can not get the taked photo");
                    }
                }
                break;
            case REQUEST_FROM_ALBUM_WITHO_OUT_CROP:
                needCrop = false;
            case REQUEST_FROM_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        final Uri photoUri = data.getData();
                        if (photoUri == null) {
                            callError(callback, "加载图片失败");
                            return;
                        }
                        try {
                            if (CompatHelper.isOverN()) {
                                if (needCrop) {
                                    //7.0用的是content uri
                                    toCrop(fragment, photoUri);
                                } else {
                                    callFinish(callback, ImageSelectUtil.getPath(fragment.getContext(), photoUri));
                                }
                            } else {
                                //非7.0用的file uri
                                String path = ImageSelectUtil.getPath(fragment.getContext(), photoUri);
                                if (needCrop) {
                                    toCrop(fragment, CompatHelper.getUri(new File(path)));
                                } else {
                                    callFinish(callback, path);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callError(callback, "图片不存在");
                        }
                    } else {
                        callError(callback, "图片不存在");
                    }
                }
                break;
            case REQUEST_FROM_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        callError(callback, "图片不存在");
                        return;
                    }
                    callFinish(callback, cropPath);
                } else {
                    callError(callback, "取消");
                }
                break;

        }
    }

    private static boolean checkTargetValided(Object obj) {
        return obj instanceof Activity || obj instanceof Fragment;
    }

    private static void callFinish(PhotoCallback callback, String filePath) {
        if (callback != null) {
            callback.onFinish(filePath);
        }
    }

    private static void callError(PhotoCallback callback, String errorMsg) {
        if (callback != null) {
            callback.onError(errorMsg);
        }
    }

    public interface PhotoCallback {
        void onFinish(String filePath);

        void onError(String msg);
    }

}

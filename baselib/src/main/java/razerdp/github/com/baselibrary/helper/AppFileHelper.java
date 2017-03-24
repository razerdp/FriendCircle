package razerdp.github.com.baselibrary.helper;

import android.os.Environment;
import android.text.TextUtils;

import com.socks.library.KLog;

import java.io.File;

import razerdp.github.com.baselibrary.base.AppContext;
import razerdp.github.com.baselibrary.utils.FileUtil;

/**
 * Created by 大灯泡 on 2017/3/23.
 * <p>
 * app文件helper
 */

public class AppFileHelper {
    private static final String TAG = "AppFileHelper";

    public static final String[] INTERNAL_STORAGE_PATHS = new String[]{"/mnt/", "/emmc/"};
    public static final String ROOT_PATH = "razerdp/github/friendcircle/";
    public static final String DATA_PATH = ROOT_PATH + ".data/";
    public static final String CACHE_PATH = ROOT_PATH + ".cache/";
    public static final String PIC_PATH = ROOT_PATH + ".pic/.nomedia/";
    public static final String LOG_PATH = ROOT_PATH + ".log/";
    public static final String TEMP_PATH = ROOT_PATH + ".temp/";

    private static String storagePath;

    public static void initStoryPath() {
        if (TextUtils.isEmpty(storagePath)) {
            //storagePath = FileUtil.getStoragePath(AppContext.getAppContext(), FileUtil.hasSDCard());
            //因为外置sd卡似乎无法写入，所以写到内置sd卡
            storagePath = FileUtil.getStoragePath(AppContext.getAppContext(), false);
            if (TextUtils.isEmpty(storagePath)) {
                storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                if (TextUtils.isEmpty(storagePath)) {
                    storagePath = AppContext.getAppContext().getFilesDir().getAbsolutePath();
                }
            }
        }

        storagePath = FileUtil.checkFileSeparator(storagePath);
        KLog.i(TAG,"storagepath  >>  "+storagePath);

        File rootDir = new File(storagePath.concat(ROOT_PATH));
        checkAndMakeDir(rootDir);

        File dataDir = new File(storagePath.concat(DATA_PATH));
        checkAndMakeDir(dataDir);

        File cacheDir = new File(storagePath.concat(CACHE_PATH));
        checkAndMakeDir(cacheDir);

        File picDir = new File(storagePath.concat(PIC_PATH));
        checkAndMakeDir(picDir);

        File logDir = new File(storagePath.concat(LOG_PATH));
        checkAndMakeDir(logDir);

        File tempDir = new File(storagePath.concat(TEMP_PATH));
        checkAndMakeDir(tempDir);

    }

    private static void checkAndMakeDir(File file) {
        if (!file.exists()) {
            KLog.i("mkdirs  >>>  " + file.getAbsolutePath());
            file.mkdirs();
        }
    }

    public static String getAppStoragePath() {
        return storagePath;
    }

    public static String getAppDataPath() {
        return storagePath.concat(DATA_PATH);
    }


    public static String getAppCachePath() {
        return storagePath.concat(CACHE_PATH);
    }

    public static String getAppPicPath() {
        return storagePath.concat(PIC_PATH);
    }

    public static String getAppLogPath() {
        return storagePath.concat(LOG_PATH);
    }

    public static String getAppTempPath() {
        return storagePath.concat(TEMP_PATH);
    }

}

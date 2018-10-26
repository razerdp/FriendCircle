package razerdp.github.com.lib.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import razerdp.github.com.lib.api.AppContext;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * preference单例
 */

public class AppSetting {

    public static final String HAS_LOGIN = "haslogin";
    public static final String HOST_NAME = "hostName";
    public static final String HOST_AVATAR = "hostAvatar";
    public static final String HOST_NICK = "hostNick";
    public static final String HOST_ID = "hostId";
    public static final String HOST_COVER = "cover";
    public static final String CHECK_REGISTER = "check_register";
    public static final String APP_HAS_SCAN_IMG = "has_scan_img";
    public static final String APP_LAST_SCAN_IMG_TIME = "last_scan_image_time";


    private static final String PERFERENCE_NAME = "FriendCircleData";
    private static SharedPreferences sharedPreferences = AppContext.getAppContext().getSharedPreferences(PERFERENCE_NAME, Context.MODE_PRIVATE);

    static {
        sharedPreferences = AppContext.getAppContext().getSharedPreferences(PERFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static String loadStringPreferenceByKey(String key, String defaultValue) {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getString(key, defaultValue);
    }

    public static boolean loadBooleanPreferenceByKey(String key, boolean defaultValue) {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static float loadFloatPreferenceByKey(String key, float defaultValue) {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static long loadLongPreferenceByKey(String key, long defaultValue) {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static Map<String, ?> loadAllPreferenceByKey() {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getAll();
    }

    public static int loadIntegerPreferenceByKey(String key, int defaultValue) {
        createSharedPreferencesIfNotExist();
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void saveStringPreferenceByKey(String key, String value) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveBooleanPreferenceByKey(String key, boolean value) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveIntegerPreferenceByKey(String key, int value) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveFloatPreferenceByKey(String key, float value) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static void saveLongPreferenceByKey(String key, long value) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void removePreferenceByKey(String key) {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void clearAllPreference() {
        createSharedPreferencesIfNotExist();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    private static void createSharedPreferencesIfNotExist() {
        if (sharedPreferences == null) {
            sharedPreferences = AppContext.getAppContext().getSharedPreferences(PERFERENCE_NAME, Context.MODE_PRIVATE);
        }
    }
}

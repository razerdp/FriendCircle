package razerdp.friendcircle.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.R.attr.data;
import static android.R.attr.type;
import static razerdp.friendcircle.utils.PreferenceHelper.Keys.*;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * preference单例
 */

public enum PreferenceHelper {
    INSTANCE;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({HOST_ID,HAS_LOGIN, HOST_AVATAR, HOST_NAME, HOST_NICK})
    public @interface Keys {
        String HAS_LOGIN = "haslogin";
        String HOST_NAME = "hostName";
        String HOST_AVATAR = "hostAvatar";
        String HOST_NICK = "hostNick";
        String HOST_ID="hostId";
    }


    private SharedPreferences sharedPreferences;
    private static final String PERFERENCE_NAME = "FriendCircleData";

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(PERFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据
     *
     * @param key
     * @param data
     */
    public void saveData(@Keys String key, Object data) {
        String type = data.getClass().getSimpleName();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }
        editor.apply();
    }

    /**
     * 得到数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public Object getData(@Keys String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }

        return null;
    }

    public void removeData(@Keys String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public boolean containsKey(@Keys String key) {
        return sharedPreferences.contains(key);
    }

}

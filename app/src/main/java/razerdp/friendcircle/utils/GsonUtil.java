package razerdp.friendcircle.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by 大灯泡 on 2016/10/28.
 *
 * gson工具类
 */

public enum GsonUtil {
    INSTANCE;
    Gson gson = new GsonBuilder().create();

    public String toString(Object obj) {
        return gson.toJson(obj);
    }

    public <T> T toObject(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> toList(String json, Type type) {
        return (ArrayList<T>) gson.fromJson(json, type);
    }
}

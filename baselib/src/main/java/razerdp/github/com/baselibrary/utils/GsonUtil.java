package razerdp.github.com.baselibrary.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
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

    @SuppressWarnings("unchecked")
    public <T> Set<T> toSet(String json, Type type) {
        Gson gson = new Gson();
        return (Set<T>) gson.fromJson(json, type);
    }

    @SuppressWarnings("unchecked")
    public <K, V> HashMap<K, V> toHashMap(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }


    @SuppressWarnings("unchecked")
    public <K, V> LinkedHashMap<K, V> toLinkHashMap(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public Gson getGson() {
        return gson;
    }
}

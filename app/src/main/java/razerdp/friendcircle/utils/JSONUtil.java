package razerdp.friendcircle.utils;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by 大灯泡 on 2016/2/25.
 * GSON解析工具类
 */
public class JSONUtil {
    public static String toString(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> toList(String json, Type type) {
        Gson gson = new Gson();
        return (ArrayList<T>) gson.fromJson(json, type);
    }
}

package razerdp.github.com.lib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * gson工具类
 */

public enum GsonUtil {
    INSTANCE;

    Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
            .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
            .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
            .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
            .registerTypeAdapter(Long.class, new LongDefaultAdapter())
            .registerTypeAdapter(long.class, new LongDefaultAdapter())
            .create();

    public String toString(Object obj) {
        return getGson().toJson(obj);
    }

    public <T> T toObject(String json, Class<T> clazz) {
        return getGson().fromJson(json, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> toList(String json, Class<T> clazz) {
        return getGson().fromJson(json, TypeList(clazz));
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> toSet(String json, Class<T> clazz) {
        return (Set<T>) getGson().fromJson(json, TypeSet(clazz));
    }

    @SuppressWarnings("unchecked")
    public <K, V> HashMap<K, V> toHashMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
        return getGson().fromJson(json, TypeHashMap(keyClazz, valueClazz));
    }

    @SuppressWarnings("unchecked")
    public <K, V> HashMap<K, V> toLinkHashMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
        return getGson().fromJson(json, TypeLinkHashMap(keyClazz, valueClazz));
    }

    @SuppressWarnings("unchecked")
    public <K, V> LinkedHashMap<K, V> toLinkHashMap(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .serializeNulls()
                    .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(Long.class, new LongDefaultAdapter())
                    .registerTypeAdapter(long.class, new LongDefaultAdapter())
                    .create();
        }
        return gson;
    }


    public static Type TypeList(Type type) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, List.class, type);
    }

    public static Type TypeSet(Type type) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, Set.class, type);
    }

    public static Type TypeHashMap(Type type, Type type2) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, HashMap.class, type, type2);
    }

    public static Type TypeLinkHashMap(Type type, Type type2) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, LinkedHashMap.class, type, type2);
    }

    public static Type TypeMap(Type type, Type type2) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, type, type2);
    }

    public static Type TypeParameterized(Type ownerType, Type rawType, Type... typeArguments) {
        return $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);
    }

    public static Type TypeArray(Type type) {
        return $Gson$Types.arrayOf(type);
    }

    public static Type TypeSubtypeOf(Type type) {
        return $Gson$Types.subtypeOf(type);
    }

    public static Type TypeSupertypeOf(Type type) {
        return $Gson$Types.supertypeOf(type);
    }


    private static class DoubleDefaultAdapter implements JsonSerializer<Double>, JsonDeserializer<Double> {
        @Override
        public Double deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为double类型,如果后台返回""或者null,则返回0.00
                    return 0.0;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsDouble();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Double aDouble, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(aDouble);
        }
    }

    private static class IntegerDefaultAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            try {
                //定义为int类型,如果后台返回""或者null,则返回0
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return 0;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Integer integer, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(integer);
        }
    }

    private static class LongDefaultAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            try {
                //定义为long类型,如果后台返回""或者null,则返回0
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return 0L;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsLong();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Long aLong, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(aLong);
        }
    }
}

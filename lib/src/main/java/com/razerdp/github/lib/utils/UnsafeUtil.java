package com.razerdp.github.lib.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by 大灯泡 on 2019/8/3.
 * <p>
 * WARN：使用unsafe直接操作内存，注意使用
 */
public class UnsafeUtil {

    private UnsafeUtil() {

    }

    private static Object unsafe;

    private static Method methodInstance;

    static {
        checkUnsafe();
    }

    private static void checkUnsafe() {
        if (unsafe != null) return;
        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field fieldUnsafe = unsafeClass.getDeclaredField("theUnsafe");
            fieldUnsafe.setAccessible(true);
            unsafe = fieldUnsafe.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (unsafe == null) {
            throw new NullPointerException("无法反射unsafe");
        }
    }

    public static <T> T unsafeInstance(Class<T> which) {
        checkUnsafe();
        try {
            if (methodInstance == null) {
                methodInstance = unsafe.getClass().getMethod("allocateInstance", Class.class);
            }
            return (T) methodInstance.invoke(unsafe, which);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

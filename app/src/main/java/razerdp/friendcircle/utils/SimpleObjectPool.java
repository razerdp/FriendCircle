package razerdp.friendcircle.utils;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 简单的对象池
 */

public class SimpleObjectPool<T> {

    private T[] objsPool;
    private int size;
    private int curPointer = -1;

    public SimpleObjectPool(int size) {
        this.size = size;
        objsPool = (T[]) new Object[size];
    }

    public synchronized T get() {
        if (curPointer == -1 || curPointer > objsPool.length) return null;
        T obj = objsPool[curPointer];
        objsPool[curPointer] = null;
        curPointer--;
        return obj;
    }

    public synchronized boolean put(T t) {
        if (curPointer == -1 || curPointer < objsPool.length - 1) {
            curPointer++;
            objsPool[curPointer] = t;
            return true;
        }
        return false;
    }

    public void clearPool() {
        for (int i = 0; i < objsPool.length; i++) {
            objsPool[i] = null;
        }
        curPointer = -1;
    }
}

package com.razerdp.github.common.modules.base;

import android.util.SparseArray;

import java.util.HashMap;

/**
 * Created by 大灯泡 on 2019/8/14.
 * <p>
 * APT生成的文件实现该接口~
 */
public interface IModuleServiceProvider {
    /**
     * Key = services impl class
     * value = services impl instance object~ 可能会有多个tag，
     */
    HashMap<Class, SparseArray<Object>> getServiceImplMap();
}

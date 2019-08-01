package com.razerdp.github.lib.helper;

import android.os.Bundle;
import android.text.TextUtils;

import com.razerdp.github.lib.utils.ActivityUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Created by 大灯泡 on 2019/8/1.
 * <p>
 * fragment注入工具
 */
public class FragmentInjectHelper {

    @Nullable
    public static <F extends Fragment> F inject(@NonNull FragmentActivity target,
                                                @NonNull Class<F> fragClass) {
        return inject(target, fragClass, null);
    }

    @Nullable
    public static <F extends Fragment> F inject(@NonNull FragmentActivity target,
                                                @NonNull Class<F> fragClass,
                                                @Nullable Bundle arguments) {
        return inject(target, fragClass, arguments, fragClass.getName());
    }

    @Nullable
    public static <F extends Fragment> F inject(@NonNull FragmentActivity target,
                                                @NonNull Class<F> fragClass,
                                                @Nullable Bundle arguments,
                                                @Nullable String tag) {
        if (!ActivityUtil.isAlive(target)) return null;
        if (TextUtils.isEmpty(tag)) {
            tag = fragClass.getName();
        }
        FragmentManager fragmentManager = target.getSupportFragmentManager();
        F result = (F) fragmentManager.findFragmentByTag(tag);
        if (result == null) {
            result = (F) fragmentManager.getFragmentFactory().instantiate(fragClass.getClassLoader(), fragClass.getName());
            result.setArguments(arguments);
            fragmentManager.beginTransaction()
                    .add(result, tag)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return result;
    }
}

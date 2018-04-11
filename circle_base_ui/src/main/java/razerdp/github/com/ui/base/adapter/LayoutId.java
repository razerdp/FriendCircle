package razerdp.github.com.ui.base.adapter;

import android.support.annotation.LayoutRes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 大灯泡 on 2018/4/10.
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutId {

    @LayoutRes
    int id();
}

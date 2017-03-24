package razerdp.github.com.baselibrary.utils.ui;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by 大灯泡 on 2017/3/16.
 */

public class ViewUtil {
    public static void setViewsClickListener(@NonNull View.OnClickListener listener, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    public static void setViewsVisible(int visible, View... views) {
        for (View view : views) {
            if (view.getVisibility() != visible) {
                view.setVisibility(visible);
            }
        }
    }

    public static void setViewsEnable(boolean enable, View... views) {
        for (View view : views) {
            view.setEnabled(enable);
        }
    }

    public static void setViewsEnableAndClickable(boolean enable,boolean clickable, View... views) {
        for (View view : views) {
            view.setEnabled(enable);
            view.setClickable(clickable);
        }
    }

}

package razerdp.github.com.baseuilib.utils;

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
}

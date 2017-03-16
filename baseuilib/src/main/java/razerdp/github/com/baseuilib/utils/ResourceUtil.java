package razerdp.github.com.baseuilib.utils;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by 大灯泡 on 2017/3/16.
 */

public class ResourceUtil {
    public static int getResourceColor(Context context, int colorResId) {
        try {
            return context.getResources().getColor(colorResId);
        } catch (Exception e) {
            return Color.TRANSPARENT;
        }
    }
}

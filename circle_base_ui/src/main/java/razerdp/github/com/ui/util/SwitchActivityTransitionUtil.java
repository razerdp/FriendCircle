package razerdp.github.com.ui.util;

import android.app.Activity;
import android.content.Context;

import razerdp.github.com.baseuilib.R;


public class SwitchActivityTransitionUtil {

    public static void transitionVerticalIn(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_translate);
    }

    public static void transitionHorizontalIn(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.no_translate);
    }

    public static void transitionVerticalOnFinish(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.no_translate, R.anim.slide_out_bottom);
    }

    public static void transitionHorizontalOnFinish(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.no_translate, R.anim.slide_out_left);
    }

    public static void transitionVerticalWithAlphaIn(Context context) {
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.slide_in_bottom_with_alpha, R.anim.no_translate);
    }

}

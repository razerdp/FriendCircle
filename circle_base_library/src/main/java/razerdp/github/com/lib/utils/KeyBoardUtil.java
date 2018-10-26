package razerdp.github.com.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import razerdp.github.com.lib.thirdpart.WeakHandler;


/**
 * Created by 大灯泡 on 2014/10/11.
 */
public class KeyBoardUtil {
    public static void open(final EditText primaryTextField) {
        new WeakHandler().postDelayed(new Runnable() {
            public void run() {
                primaryTextField.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_DOWN, 0, 0, 0));
                primaryTextField.dispatchTouchEvent(MotionEvent.obtain(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP, 0, 0, 0));
                if (primaryTextField.getText() != null) {
                    String text = primaryTextField.getText().toString();
                    if (!TextUtils.isEmpty(text)) {
                        primaryTextField.setSelection(text.length());
                    }
                }
            }
        }, 100);
    }

    /**
     * 隐藏软键盘
     */
    public static void close(Activity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getWindow().getDecorView().getRootView();
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void close(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close(final View view, long delayMillis) {
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                close(view);
            }
        }, delayMillis);
    }
}

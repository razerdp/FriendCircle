package razerdp.friendcircle.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import razerdp.friendcircle.app.interfaces.OnSoftKeyboardChangeListener;

/**
 * Created by 大灯泡 on 2016/2/9.
 * ui工具类
 */
public class UIHelper {
    /**
     * dip转px
     */
    public static int dipToPx(Context context, float dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dip
     */
    public static int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕分辨率：宽
     */
    public static int getScreenPixWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕分辨率：高
     */
    public static int getScreenPixHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 隐藏软键盘
     */
    public static void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideInputMethod(final View view, long delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideInputMethod(view);
            }
        }, delayMillis);
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 多少时间后显示软键盘
     */
    public static void showInputMethod(final View view, long delayMillis) {
        // 显示输入法
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                UIHelper.showInputMethod(view);
            }
        }, delayMillis);
    }

    /**
     * 监听软键盘高度和状态
     *
     * source web link:
     * http://blog.csdn.net/daguaio_o/article/details/47127993
     */
    public static void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangeListener listener) {
        final View decorView = activity.getWindow().getDecorView();
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                int previousKeyboardHeight = -1;

                @Override
                public void onGlobalLayout() {
                    Rect rect = new Rect();
                    decorView.getWindowVisibleDisplayFrame(rect);
                    int displayHeight = rect.bottom - rect.top;
                    int height = decorView.getHeight();
                    int keyboardHeight = height - displayHeight;
                    if (previousKeyboardHeight != keyboardHeight) {
                        boolean hide = (double) displayHeight / height > 0.8;
                        listener.onSoftKeyBoardChange(keyboardHeight, !hide);
                    }
                    previousKeyboardHeight = height;

                }
            });
        }
}

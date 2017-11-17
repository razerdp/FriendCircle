package razerdp.github.com.lib.manager;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

/**
 * Created by 大灯泡 on 2016/12/13.
 *
 * 键盘管理
 */

public class KeyboardControlMnanager {


    private OnKeyboardStateChangeListener onKeyboardStateChangeListener;
    private WeakReference<Activity> act;

    private KeyboardControlMnanager(Activity act, OnKeyboardStateChangeListener onKeyboardStateChangeListener) {
        this.act = new WeakReference<Activity>(act);
        this.onKeyboardStateChangeListener = onKeyboardStateChangeListener;
    }


    public void observerKeyboardVisibleChangeInternal() {
        if (onKeyboardStateChangeListener == null) return;
        Activity activity = act.get();
        if (activity == null) return;
        setOnKeyboardStateChangeListener(onKeyboardStateChangeListener);
        final View decorView = activity.getWindow().getDecorView();

        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int preKeyboardHeight = -1;
            Rect rect = new Rect();
            boolean preVisible = false;

            @Override
            public void onGlobalLayout() {
                rect.setEmpty();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.height();
                int windowHeight = decorView.getHeight();
                int keyboardHeight = windowHeight - displayHeight;
                if (preKeyboardHeight != keyboardHeight) {
                    //判定可见区域与原来的window区域占比是否小于0.75,小于意味着键盘弹出来了。
                    boolean isVisible = (displayHeight * 1.0f / windowHeight * 1.0f) < 0.75f;
                    if (isVisible != preVisible) {
                        onKeyboardStateChangeListener.onKeyboardChange(keyboardHeight, isVisible);
                        preVisible = isVisible;
                    }
                }
                preKeyboardHeight = keyboardHeight;
            }
        });


    }

    public static void observerKeyboardVisibleChange(Activity act, OnKeyboardStateChangeListener onKeyboardStateChangeListener) {
        new KeyboardControlMnanager(act, onKeyboardStateChangeListener).observerKeyboardVisibleChangeInternal();

    }

    public OnKeyboardStateChangeListener getOnKeyboardStateChangeListener() {
        return onKeyboardStateChangeListener;
    }

    public void setOnKeyboardStateChangeListener(OnKeyboardStateChangeListener onKeyboardStateChangeListener) {
        this.onKeyboardStateChangeListener = onKeyboardStateChangeListener;
    }

    //=============================================================interface
    public interface OnKeyboardStateChangeListener {
        void onKeyboardChange(int keyboardHeight, boolean isVisible);
    }
}

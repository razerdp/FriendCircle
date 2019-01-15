package razerdp.friendcircle.ui.helper;

import android.graphics.Color;
import android.view.View;
import android.view.ViewTreeObserver;

import razerdp.github.com.ui.widget.common.TitleBar;

/**
 * Created by 大灯泡 on 2019/1/15.
 * <p>
 * titlebar alpha 变化帮助类
 */
public class TitleBarAlphaChangeHelper {

    private TitleBar mTitleBar;
    private View which;
    private int scrolledAlphaCriticalOffset;
    private int fullAlphaBackgroundColor = Color.parseColor("#EBE9E6");

    private TitleBarAlphaChangeHelper(TitleBar titleBar, View which) {
        this.mTitleBar = titleBar;
        this.which = which;
        check();
        handleInternal();
    }

    public static TitleBarAlphaChangeHelper handle(TitleBar titleBar, View which) {
        return new TitleBarAlphaChangeHelper(titleBar, which);
    }

    private void check() {
        if (mTitleBar == null || which == null) return;
        which.post(new Runnable() {
            @Override
            public void run() {
                if (scrolledAlphaCriticalOffset == 0) {
                    scrolledAlphaCriticalOffset = Math.abs(which.getTop() - mTitleBar.getBottom());
                }
                handleInternal();
            }
        });
    }

    private void handleInternal() {
        which.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = which.getScrollY();
                if (scrollY > 0) {
                    float alpha = Math.min((float) scrollY / scrolledAlphaCriticalOffset, 1f);

                }


            }
        });
    }
}

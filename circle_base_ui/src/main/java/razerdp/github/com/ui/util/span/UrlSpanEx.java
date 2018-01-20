package razerdp.github.com.ui.util.span;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

/**
 * Created by 大灯泡 on 2017/10/30.
 */

public class UrlSpanEx extends URLSpan {
    int color = -1;
    private View.OnClickListener mOnClickListener;

    public UrlSpanEx(String url) {
        super(url);
    }

    public UrlSpanEx(Parcel src) {
        super(src);
    }

    public UrlSpanEx(String url, int color) {
        super(url);
        this.color = color;
    }

    public UrlSpanEx(String url, int color, View.OnClickListener onClickListener) {
        super(url);
        this.color = color;
        this.mOnClickListener = onClickListener;
    }

    public UrlSpanEx(Parcel src, int color) {
        super(src);
        this.color = color;
    }


    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void onClick(View widget) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(widget);
        } else {
            super.onClick(widget);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (color != -1) {
            ds.setColor(color);
        }
        ds.setUnderlineText(false);
    }

}

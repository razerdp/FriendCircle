package razerdp.github.com.ui.widget.common;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.baseuilib.R;
import razerdp.github.com.ui.util.ViewUtil;
import razerdp.github.com.ui.widget.indicator.DotWidget;

/**
 * Created by 大灯泡 on 2016/7/20.
 * <p>
 * titlebar
 */

public class TitleBar extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {

    private LinearLayout ll_left;
    private ImageView iv_left;
    private TextView tx_left;
    private LinearLayout ll_right;
    private ImageView iv_right;
    private TextView tx_right;
    private TextView title;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_LEFT, MODE_RIGHT, MODE_BOTH, MODE_TITLE})
    public @interface TitleBarMode {
    }

    public static final int MODE_LEFT = 0x11;
    public static final int MODE_RIGHT = 0x12;
    public static final int MODE_BOTH = 0x13;
    public static final int MODE_TITLE = 0x14;

    private int currentMode = MODE_LEFT;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        View.inflate(context, R.layout.widget_title_bar, this);
        ll_left = (LinearLayout) findViewById(R.id.ll_title_bar_left);
        iv_left = (ImageView) findViewById(R.id.ic_title_bar_left);
        tx_left = (TextView) findViewById(R.id.tx_title_bar_left);

        ll_right = (LinearLayout) findViewById(R.id.ll_title_bar_right);
        iv_right = (ImageView) findViewById(R.id.ic_title_bar_right);
        tx_right = (TextView) findViewById(R.id.tx_title_bar_right);

        title = (TextView) findViewById(R.id.tx_title);
        this.setBackgroundColor(UIHelper.getColor(R.color.action_bar_bg));

        ll_right.setOnClickListener(null);
        ll_right.setVisibility(INVISIBLE);
        ll_left.setOnClickListener(this);
        title.setOnLongClickListener(this);

    }

    public int getTitleBarMode() {
        return currentMode;
    }

    public void setTitleBarMode(@TitleBarMode int currentMode) {
        if (this.currentMode == currentMode) return;
        this.currentMode = currentMode;
        switch (currentMode) {
            case MODE_LEFT:
                ll_left.setVisibility(VISIBLE);
                ll_right.setOnClickListener(null);
                ll_right.setOnLongClickListener(null);
                ll_right.setVisibility(INVISIBLE);
                ll_left.setOnClickListener(this);
                ll_left.setOnLongClickListener(this);
                break;
            case MODE_RIGHT:
                ll_right.setVisibility(VISIBLE);
                ll_left.setOnClickListener(null);
                ll_left.setOnLongClickListener(null);
                ll_left.setVisibility(INVISIBLE);
                ll_right.setOnClickListener(this);
                ll_right.setOnLongClickListener(this);
                break;
            case MODE_BOTH:
                ll_left.setVisibility(VISIBLE);
                ll_right.setVisibility(VISIBLE);
                ViewUtil.setViewsClickListener(this, ll_left, ll_right);
                ll_left.setOnLongClickListener(this);
                ll_right.setOnLongClickListener(this);
                break;
            case MODE_TITLE:
                ll_left.setVisibility(GONE);
                ll_right.setVisibility(GONE);
                ll_left.setOnClickListener(null);
                ll_right.setOnClickListener(null);
                break;
            default:
                break;
        }
    }

    public void setTitleBarBackground(int color) {
        if (color != -1) {
            this.setBackgroundColor(color);
        }
    }

    public TextView getTitleView() {
        return title;
    }

    public void setTitle(String titleStr) {
        if (TextUtils.isEmpty(titleStr)) {
            title.setText("");
        } else {
            title.setText(titleStr);
        }
    }

    public void setTitle(int stringResid) {
        if (stringResid > 0) {
            title.setText(stringResid);
        }
    }

    public void setLeftIcon(int resid) {
        try {
            iv_left.setImageResource(resid);
            setShowLeftIcon(resid != 0);
        } catch (Exception e) {
            KLog.e(e);
        }
    }

    public void setRightIcon(int resid) {
        try {
            iv_right.setImageResource(resid);
            setShowRightIcon(resid != 0);
        } catch (Exception e) {
            KLog.e(e);
        }
    }

    public void setLeftText(int resid) {
        if (resid > 0) {
            tx_left.setText(resid);
        }
    }

    public void setLeftText(String leftText) {
        if (TextUtils.isEmpty(leftText)) {
            tx_left.setText("");
            tx_left.setVisibility(GONE);
        } else {
            tx_left.setText(leftText);
        }
    }

    public void setLeftTextColor(int color) {
        tx_left.setTextColor(color);
    }

    public void setRightTextColor(int color) {
        tx_right.setTextColor(color);
    }


    public void setRightText(int resid) {
        if (resid > 0) {
            if (tx_right.getVisibility() != VISIBLE) {
                tx_right.setVisibility(VISIBLE);
            }
            tx_right.setText(resid);
        }
    }

    public void setRightText(String leftText) {
        if (TextUtils.isEmpty(leftText)) {
            tx_right.setText("");
            tx_right.setVisibility(GONE);
        } else {
            if (tx_right.getVisibility() != VISIBLE) {
                tx_right.setVisibility(VISIBLE);
            }
            tx_right.setText(leftText);
        }
    }

    public void setShowLeftIcon(boolean isShow) {
        iv_left.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setShowRightIcon(boolean isShow) {
        iv_right.setVisibility(isShow ? VISIBLE : GONE);
    }

    DotWidget leftDotWidget;

    public void setLeftRedDotShow(boolean isShow) {
        if (iv_left.getVisibility() != VISIBLE) return;
        if (leftDotWidget == null) {
            leftDotWidget = new DotWidget(getContext(), iv_left);
            leftDotWidget.setDotColor(Color.RED);
            leftDotWidget.setDotSize(UIHelper.dipToPx(3f));
            leftDotWidget.setMode(DotWidget.Mode.CIRCLE);
        }
        if (isShow) {
            leftDotWidget.show();
        } else {
            leftDotWidget.hide(true);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_bar_left) {
            if (onTitleBarClickListener != null) {
                onTitleBarClickListener.onLeftClick(ll_left, false);
            }

        } else if (i == R.id.ll_title_bar_right) {
            if (onTitleBarClickListener != null) {
                onTitleBarClickListener.onRightClick(ll_left, false);
            }

        }
    }


    @Override
    public boolean onLongClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_bar_left) {
            if (onTitleBarClickListener != null) {
                return onTitleBarClickListener.onLeftClick(ll_left, true);
            }

        } else if (i == R.id.ll_title_bar_right) {
            if (onTitleBarClickListener != null) {
                return onTitleBarClickListener.onRightClick(ll_left, true);
            }

        } else if (i == R.id.tx_title) {
            if (onTitleBarClickListener != null) {
                return onTitleBarClickListener.onTitleLongClick(v);
            }
        }
        return false;
    }

    private OnTitleBarClickListener onTitleBarClickListener;

    public OnTitleBarClickListener getOnTitleBarClickListener() {
        return onTitleBarClickListener;
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener onTitleBarClickListener) {
        this.onTitleBarClickListener = onTitleBarClickListener;
    }

    public interface OnTitleBarClickListener {
        boolean onLeftClick(View v, boolean isLongClick);

        boolean onTitleLongClick(View v);

        boolean onRightClick(View v, boolean isLongClick);
    }
}

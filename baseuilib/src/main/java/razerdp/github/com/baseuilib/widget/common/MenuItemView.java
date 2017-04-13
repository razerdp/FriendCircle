package razerdp.github.com.baseuilib.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import razerdp.github.com.baselibrary.base.AppContext;
import razerdp.github.com.baseuilib.R;

/**
 * Created by 大灯泡 on 2017/4/13.
 * <p>
 * 通用的菜单选项
 */

public class MenuItemView extends FrameLayout {
    private static final String TAG = "MenuItemView";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HIDE_NONE, HIDE_LEFT_ICON, HIDE_MAIN_TEXT, HIDE_SUBTITLE_TEXT, HIDE_RIGHT_ICON, HIDE_ALL})
    public @interface HideMode {
    }

    public static final int HIDE_NONE = -1;
    public static final int HIDE_LEFT_ICON = 1;
    public static final int HIDE_MAIN_TEXT = 2;
    public static final int HIDE_SUBTITLE_TEXT = 4;
    public static final int HIDE_RIGHT_ICON = 8;
    public static final int HIDE_ALL = 15;

    private int hideMode = HIDE_NONE;
    private int mLeftIconRes;
    private int mRightIconRes;
    private String mMainText;
    private String mSubtitleText;
    private int mMainTextColor;
    private int mSubtitleTextColor;
    private float mMainTextSize;
    private float mSubtitleTextSize;


    private ImageView leftIcon;
    private ImageView rightIcon;
    private TextView mainText;
    private TextView subtitleText;

    public MenuItemView(Context context) {
        this(context, null);
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        initFromAttributes(context, attrs);
        initInternal(context);
    }

    private void initFromAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MenuItemView, 0, 0);

        mLeftIconRes = a.getResourceId(R.styleable.MenuItemView_ic_left, View.NO_ID);
        mRightIconRes = a.getResourceId(R.styleable.MenuItemView_ic_right, View.NO_ID);
        mMainText = a.getString(R.styleable.MenuItemView_tx_main);
        mSubtitleText = a.getString(R.styleable.MenuItemView_tx_subtitle);
        mMainTextColor = a.getColor(R.styleable.MenuItemView_tx_main_color, Color.BLACK);
        mSubtitleTextColor = a.getColor(R.styleable.MenuItemView_tx_subtitle_color, 0xff9fa3a7);
        mMainTextSize = a.getDimensionPixelSize(R.styleable.MenuItemView_tx_main_size, 0);
        mSubtitleTextSize = a.getDimensionPixelSize(R.styleable.MenuItemView_tx_subtitle_size, 0);
        hideMode = a.getInt(R.styleable.MenuItemView_menu_hide_item, -1);
        Drawable background = a.getDrawable(com.android.internal.R.styleable.View_background);
        if (background == null) {
            setBackgroundResource(R.drawable.common_selector);
        }
        a.recycle();
    }

    private void setDefaultValue() {
        if (mLeftIconRes == View.NO_ID) mLeftIconRes = View.NO_ID;
        if (mRightIconRes == View.NO_ID) mRightIconRes = R.drawable.arrow_gray;
        if (mMainTextSize == 0) mMainTextSize = sp2Px(18);
        if (mSubtitleTextSize == 0) mSubtitleTextSize = sp2Px(18);

        setLeftIconResource(mLeftIconRes);
        setRightIconResource(mRightIconRes);
        setMainText(mMainText);
        setSubtitleText(mSubtitleText);
        setMainTextColor(mMainTextColor);
        setSubtitleTextColor(mSubtitleTextColor);
        setMainTextSize(mMainTextSize);
        setSubtitleTextSize(mSubtitleTextSize);
        setHideMode(hideMode);
    }

    private void initInternal(Context context) {
        View.inflate(context, R.layout.widget_menu_item, this);
        leftIcon = (ImageView) findViewById(R.id.menu_ic_left);
        rightIcon = (ImageView) findViewById(R.id.menu_ic_right);
        mainText = (TextView) findViewById(R.id.menu_tx_main);
        subtitleText = (TextView) findViewById(R.id.menu_tx_subtitle);
        setDefaultValue();
    }


    public void setLeftIconResource(int resourceid) {
        this.mLeftIconRes = resourceid;
        if (resourceid == View.NO_ID) {
            setLeftIconVisibility(GONE);
        } else {
            setLeftIconVisibility(VISIBLE);
            leftIcon.setImageResource(resourceid);
        }
    }

    public void setLeftIconVisibility(int visibility) {
        leftIcon.setVisibility(visibility);
    }

    public void setRightIconResource(int resourceid) {
        this.mRightIconRes = resourceid;
        if (resourceid == View.NO_ID) {
            setRightIconVisibility(GONE);
        } else {
            setRightIconVisibility(VISIBLE);
            rightIcon.setImageResource(resourceid);
        }
    }

    public void setRightIconVisibility(int visibility) {
        rightIcon.setVisibility(visibility);
    }

    public void setMainText(String str) {
        mainText.setText(str);
    }

    public void setSubtitleText(String str) {
        subtitleText.setText(str);
    }

    public void setMainTextColor(int color) {
        mainText.setTextColor(color);
    }

    public void setSubtitleTextColor(int color) {
        subtitleText.setTextColor(color);
    }

    public void setMainTextSize(float spSize) {
        mainText.setTextSize(sp2Px(spSize));
    }

    public void setSubtitleTextSize(float spSize) {
        subtitleText.setTextSize(sp2Px(spSize));
    }

    public void setHideMode(@HideMode int... modes) {
        int mode = 0;
        for (int i : modes) {
            mode += i;
        }
        setHideModeInternal(mode);
    }

    private void setHideModeInternal(int mode) {
        if (mode == HIDE_NONE) {
            setViewsVisible(VISIBLE, leftIcon, rightIcon, mainText, subtitleText);
        } else if (mode == HIDE_LEFT_ICON) {
            setLeftIconVisibility(GONE);
        } else if (mode == HIDE_MAIN_TEXT) {
            setViewsVisible(GONE, mainText);
        } else if (mode == HIDE_SUBTITLE_TEXT) {
            setViewsVisible(GONE, subtitleText);
        } else if (mode == HIDE_RIGHT_ICON) {
            setRightIconVisibility(GONE);
        } else if (mode == HIDE_LEFT_ICON + HIDE_MAIN_TEXT) {
            setViewsVisible(GONE, leftIcon, mainText);
        } else if (mode == HIDE_LEFT_ICON + HIDE_MAIN_TEXT + HIDE_SUBTITLE_TEXT) {
            setViewsVisible(GONE, leftIcon, mainText, subtitleText);
        } else if (mode == HIDE_LEFT_ICON + HIDE_SUBTITLE_TEXT) {
            setViewsVisible(GONE, leftIcon, subtitleText);
        } else if (mode == HIDE_LEFT_ICON + HIDE_RIGHT_ICON) {
            setViewsVisible(GONE, leftIcon, rightIcon);
        } else if (mode == HIDE_MAIN_TEXT + HIDE_SUBTITLE_TEXT) {
            setViewsVisible(GONE, mainText, subtitleText);
        } else if (mode == HIDE_MAIN_TEXT + HIDE_RIGHT_ICON) {
            setViewsVisible(GONE, mainText, rightIcon);
        } else if (mode == HIDE_SUBTITLE_TEXT + HIDE_RIGHT_ICON) {
            setViewsVisible(GONE, subtitleText, rightIcon);
        } else if (mode == HIDE_ALL) {
            setViewsVisible(GONE, this);
        } else {
            setViewsVisible(VISIBLE, leftIcon, rightIcon, mainText, subtitleText);
        }
        this.hideMode = mode;
    }

    private void setViewsVisible(int visible, View... views) {
        for (View view : views) {
            if (view.getVisibility() != visible) {
                view.setVisibility(visible);
            }
        }
    }

    private final float sp2Px(float sp) {
        final float fontScale = AppContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }


}

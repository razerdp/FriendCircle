package razerdp.github.com.ui.base;

import android.text.TextUtils;
import android.view.View;

import razerdp.github.com.baseuilib.R;
import razerdp.github.com.lib.interfaces.MultiClickListener;
import razerdp.github.com.ui.widget.common.TitleBar;

/**
 * Created by 大灯泡 on 2018/10/26.
 */
public abstract class BaseTitleBarFragment extends BaseStatusControlFragment {
    protected TitleBar titleBar;

    @Override
    protected void onPreInitView(View rootView) {
        if (titleBar == null) titleBar = rootView.findViewById(R.id.title_bar_view);
        if (titleBar != null) {
            titleBar.setOnClickListener(new MultiClickListener() {
                @Override
                public void onSingleClick() {
                    onTitleSingleClick();
                }

                @Override
                public void onDoubleClick() {
                    onTitleDoubleClick();
                }
            });
            titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
                @Override
                public boolean onLeftClick(View v, boolean isLongClick) {
                    if (!isLongClick) {
                        onTitleLeftClick();
                        return false;
                    } else {
                        return onTitleLeftLongClick();
                    }
                }

                @Override
                public boolean onTitleLongClick(View v) {
                    return BaseTitleBarFragment.this.onTitleLongClick(v);
                }

                @Override
                public boolean onRightClick(View v, boolean isLongClick) {
                    if (!isLongClick) {
                        onTitleRightClick();
                        return false;
                    } else {
                        return onTitleRightLongClick();
                    }
                }
            });
        }
    }

    public boolean onTitleLongClick(View v) {
        return false;
    }

    public boolean onTitleLeftLongClick() {
        return false;
    }

    public boolean onTitleRightLongClick() {
        return false;
    }

    public void onTitleLeftClick() {
        back();
    }

    public void onTitleRightClick() {
    }

    public void onTitleDoubleClick() {
    }

    public void onTitleSingleClick() {
    }

    public void setTitle(int resId) {
        if (titleBar != null && resId != 0) {
            titleBar.setTitleText(resId);
        }
    }

    public void setTitle(String title) {
        if (titleBar != null && !TextUtils.isEmpty(title)) {
            titleBar.setTitleText(title);
        }
    }

    public void setTitleMode(@TitleBar.TitleBarMode int mode) {
        if (titleBar != null) {
            titleBar.setMode(mode);
        }
    }

    public void setTitleRightText(String text) {
        if (titleBar != null) {
            titleBar.setRightText(text);
        }
    }

    public void setTitleRightIcon(int resid) {
        if (titleBar != null) {
            titleBar.setRightIcon(resid);
        }
    }

    public void setTitleLeftText(String text) {
        if (titleBar != null) {
            titleBar.setLeftText(text);
        }
    }

    public void setTitleLeftIcon(int resid) {
        if (titleBar != null) {
            titleBar.setLeftIcon(resid);
        }
    }

    public void setLeftTextColor(int color) {
        if (titleBar != null) {
            titleBar.setLeftTextColor(color);
        }
    }

    public void setRightTextColor(int color) {
        if (titleBar != null) {
            titleBar.setRightTextColor(color);
        }
    }

    public void setTitleBarBackground(int color) {
        if (titleBar != null) {
            titleBar.setTitleBarBackgroundColor(color);
        }
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public String getBarTitle() {
        if (titleBar != null) {
            return titleBar.getTitleView().getText().toString();
        }
        return null;
    }
}

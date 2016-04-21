package razerdp.friendcircle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import razerdp.friendcircle.utils.UIHelper;

/**
 * Created by 大灯泡 on 2016/4/21.
 * viewpager图片浏览器底部的小点点指示器
 */
public class DotIndicator extends LinearLayout {
    private static final String TAG = "DotIndicator";

    List<DotView> mDotViews;

    private int currentSelection = 0;

    private int mDotsNum = 9;

    public DotIndicator(Context context) {
        this(context,null);
    }

    public DotIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DotIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        buildDotView(context);
    }

    /**
     * 初始化dotview
     * @param context
     */
    private void buildDotView(Context context) {
        mDotViews = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            DotView dotView = new DotView(context);
            dotView.setSelected(false);
            LinearLayout.LayoutParams params = new LayoutParams(UIHelper.dipToPx(context, 10f),
                    UIHelper.dipToPx(context, 10f));
            if (i == 0) {
                params.leftMargin = 0;
            }
            else {
                params.leftMargin = UIHelper.dipToPx(context, 6f);
            }
            addView(dotView,params);
            mDotViews.add(dotView);
        }
    }

    /**
     * 当前选中的dotview
     * @param selection
     */
    public void setCurrentSelection(int selection) {
        this.currentSelection = selection;
        for (DotView dotView : mDotViews) {
            dotView.setSelected(false);
        }
        if (selection >= 0 && selection < mDotViews.size()) {
            mDotViews.get(selection).setSelected(true);
        }
        else {
            Log.e(TAG, "the selection can not over dotViews size");
        }
    }

    public int getCurrentSelection() {
        return currentSelection;
    }

    /**
     * 当前需要展示的dotview数量
     * @param num
     */
    public void setDotViewNum(int num) {
        if (num > 9 || num <= 0) {
            Log.e(TAG, "num必须在1~9之间哦");
            return;
        }

        for (DotView dotView : mDotViews) {
            dotView.setVisibility(VISIBLE);
        }
        this.mDotsNum = num;
        for (int i = num; i < mDotViews.size(); i++) {
            DotView dotView = mDotViews.get(i);
            if (dotView != null) {
                dotView.setSelected(false);
                dotView.setVisibility(GONE);
            }
        }
    }

    public int getDotViewNum() {
        return mDotsNum;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDotViews.clear();
        mDotViews=null;
        Log.d(TAG, "清除dotview引用");
    }
}

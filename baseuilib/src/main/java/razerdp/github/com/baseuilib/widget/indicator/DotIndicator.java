package razerdp.github.com.baseuilib.widget.indicator;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import razerdp.github.com.baselibrary.utils.ui.UIHelper;


/**
 * Created by 大灯泡 on 2016/6/3.
 * <p/>
 * banner指示器容器
 */
public class DotIndicator extends LinearLayout {
    private static final String TAG = "DotIndicator";
    private static final int DEFAULT_DOT_NUM = 0;
    private static final int MAX_DOT_NUM = 9;
    private static final int DOT_SIZE = 8;

    List<DotView> mDotViews;

    private int currentSelection = 0;

    private int mDotsNum = DEFAULT_DOT_NUM;

    public DotIndicator(Context context) {
        this(context, null);
    }

    public DotIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DotIndicator init(Context context, int dotNum) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        this.mDotsNum = dotNum == 0 ? DEFAULT_DOT_NUM : dotNum;
        buildDotView(context);
        if (mDotsNum <= 1) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
        setCurrentSelection(0);
        KLog.i(TAG, "new instance");
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDotViews.size() == 0) {
            removeAllViews();
            buildDotView(getContext());
            setCurrentSelection(currentSelection);
        }
    }

    /**
     * 初始化dotview
     */
    private void buildDotView(Context context) {
        mDotViews = new ArrayList<>();
        for (int i = 0; i < mDotsNum; i++) {
            DotView dotView = new DotView(context);
            dotView.setSelected(false);
            LinearLayout.LayoutParams params = new LayoutParams(UIHelper.dipToPx(DOT_SIZE), UIHelper.dipToPx(DOT_SIZE));
            if (i == 0) {
                params.leftMargin = 0;
            } else {
                params.leftMargin = UIHelper.dipToPx(6f);
            }
            addView(dotView, params);
            mDotViews.add(dotView);
        }
    }

    public int getCurrentSelection() {
        return currentSelection;
    }

    /**
     * 当前选中的dotview
     */
    public void setCurrentSelection(int selection) {
        this.currentSelection = selection;
        for (DotView dotView : mDotViews) {
            dotView.setSelected(false);
        }
        if (selection >= 0 && selection < mDotViews.size()) {
            mDotViews.get(selection).setSelected(true);
        } else {
            Log.e(TAG, "the selection can not over dotViews size");
        }
    }

    public int getDotViewNum() {
        return mDotsNum;
    }

    /**
     * 当前需要展示的dotview数量
     */
    public void setDotViewNum(int num) {
        if (num > MAX_DOT_NUM || num <= 0) {
            KLog.e(TAG, "num必须在1~" + MAX_DOT_NUM + "之间哦");
            return;
        }
        if (num <= 1) {
            removeAllViews();
            setVisibility(GONE);
        }

        if (this.mDotsNum != num) {
            this.mDotsNum = num;
            removeAllViews();
            mDotViews.clear();
            mDotViews = null;
            buildDotView(getContext());
            setCurrentSelection(currentSelection);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDotViews != null) {
            mDotViews.clear();
            KLog.i(TAG, "清除dotindicator引用");
        }
    }
}

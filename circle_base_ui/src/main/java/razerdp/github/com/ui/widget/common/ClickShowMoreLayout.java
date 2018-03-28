package razerdp.github.com.ui.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.baseuilib.R;
import razerdp.github.com.ui.widget.commentwidget.ExTextView;

/**
 * Created by 大灯泡 on 2016/2/19.
 * 点击展开更多
 */
public class ClickShowMoreLayout extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "ClickShowMoreLayout";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CLOSE, OPEN})
    public @interface State {

    }

    public static final int CLOSE = 0;
    public static final int OPEN = 1;

    private int preState;

    private ExTextView mTextView;
    private TextView mClickToShow;

    private int textColor;
    private int textSize;

    private int showLine;
    private String clickText;

    private static final SparseIntArray TEXT_STATE = new SparseIntArray();

    private OnStateKeyGenerateListener mOnStateKeyGenerateListener;

    public ClickShowMoreLayout(Context context) {
        this(context, null);
    }

    public ClickShowMoreLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickShowMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClickShowMoreLayout);
        textColor = a.getColor(R.styleable.ClickShowMoreLayout_text_color, 0xff1a1a1a);
        textSize = a.getDimensionPixelSize(R.styleable.ClickShowMoreLayout_text_size, 14);
        showLine = a.getInt(R.styleable.ClickShowMoreLayout_show_line, 8);
        clickText = a.getString(R.styleable.ClickShowMoreLayout_click_text);
        if (TextUtils.isEmpty(clickText)) clickText = "全文";
        a.recycle();

        initView(context);

    }

    private void initView(Context context) {
        mTextView = new ExTextView(context);
        mClickToShow = new TextView(context);

        mTextView.setTextSize(textSize);
        mTextView.setTextColor(textColor);
        mTextView.setMaxLines(showLine);

        mClickToShow.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tx_show_more));
        mClickToShow.setTextSize(textSize);
        mClickToShow.setTextColor(getResources().getColor(R.color.nick));
        mClickToShow.setText(clickText);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = UIHelper.dipToPx(10f);
        mClickToShow.setLayoutParams(params);
        mClickToShow.setOnClickListener(this);

        setOrientation(VERTICAL);
        addView(mTextView);
        addView(mClickToShow);
    }

    @Override
    public void onClick(View v) {
        boolean needOpen = TextUtils.equals(((TextView) v).getText().toString(), clickText);
        setState(needOpen ? OPEN : CLOSE);
    }


    public void setState(@State int state) {
        setStateInternal(getStateKey(getText().toString()), state);
    }

    private void setStateInternal(int key, int state) {
        switch (state) {
            case CLOSE:
                mTextView.setMaxLines(showLine);
                mClickToShow.setText(clickText);
                break;
            case OPEN:
                mTextView.setMaxLines(Integer.MAX_VALUE);
                mClickToShow.setText("收起");
                break;
        }
        TEXT_STATE.put(key, state);
    }

    public void setText(String str) {
        restoreState(str);
        mTextView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                boolean hasMore = mTextView.getLineCount() > showLine;
                mClickToShow.setVisibility(hasMore ? VISIBLE : GONE);
                mTextView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
        mTextView.setText(str);
    }

    private void restoreState(String str) {
        int state = CLOSE;
        final int keyId = getStateKey(str);
        int holderState = TEXT_STATE.get(keyId, -1);
        if (holderState != -1) {
            state = holderState;
        }
        setStateInternal(keyId, state);
    }

    private int getStateKey(String str) {
        if (str == null) return -1;
        //通过viewParent生成key，相当于该控件的string与viewParent绑定
        //但是在列表中似乎因为复用问题会导致状态不准确
        ViewParent parent = getParent();
        int parentId = 0;
        if (parent != null) {
            parentId = parent.hashCode();
        }
        int result = str.hashCode() + parentId;
        if (mOnStateKeyGenerateListener != null) {
            result = mOnStateKeyGenerateListener.onStateKeyGenerated(result);
        }
        return result;
    }

    public CharSequence getText() {
        return mTextView.getText();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if (mTextView != null) {
            mTextView.setTextColor(textColor);
        }
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getClickText() {
        return clickText;
    }

    public void setClickText(String clickText) {
        this.clickText = clickText;
        if (mClickToShow != null) {
            mClickToShow.setText(clickText);
        }
    }

    public void setClickTextColor(@ColorInt int color) {
        if (mClickToShow != null) {
            mClickToShow.setTextColor(color);
        }
    }

    public OnStateKeyGenerateListener getOnStateKeyGenerateListener() {
        return mOnStateKeyGenerateListener;
    }

    public void setOnStateKeyGenerateListener(OnStateKeyGenerateListener onStateKeyGenerateListener) {
        mOnStateKeyGenerateListener = onStateKeyGenerateListener;
    }

    public interface OnStateKeyGenerateListener {
        int onStateKeyGenerated(int originKey);
    }
}

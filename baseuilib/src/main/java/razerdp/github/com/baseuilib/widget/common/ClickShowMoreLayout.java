package razerdp.github.com.baseuilib.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baseuilib.R;

/**
 * Created by 大灯泡 on 2016/2/19.
 * 点击展开更多
 */
public class ClickShowMoreLayout extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "ClickShowMoreLayout";


    public static final int CLOSE = 0;
    public static final int OPEN = 1;

    private int preState;

    private TextView mTextView;
    private TextView mClickToShow;

    private int textColor;
    private int textSize;

    private int showLine;
    private String clickText;

    private boolean hasMore;
    private boolean hasGetLineCount = false;


    private static final SparseIntArray TEXT_STATE = new SparseIntArray();

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
        mTextView = new TextView(context);
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


    public void setState(int state) {
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
        TEXT_STATE.put(getText().toString().hashCode(), state);
    }

    public void setText(String str) {
        if (hasGetLineCount) {
            restoreState(str);
            mTextView.setText(str);
        } else {
            mTextView.setText(str);
            mTextView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (!hasGetLineCount) {
                        hasMore = mTextView.getLineCount() > showLine;
                        hasGetLineCount = true;
                    }
                    mClickToShow.setVisibility(hasMore ? VISIBLE : GONE);
                    mTextView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
            setState(CLOSE);
        }
    }

    private void restoreState(String str) {
        int state = CLOSE;
        int holderState = TEXT_STATE.get(str.hashCode(), -1);
        if (holderState == -1) {
            TEXT_STATE.put(str.hashCode(), state);
        } else {
            state = holderState;
        }
        setState(state);
    }

    public CharSequence getText() {
        return mTextView.getText();
    }

}

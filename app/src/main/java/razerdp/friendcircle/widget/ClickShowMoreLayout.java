package razerdp.friendcircle.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import razerdp.friendcircle.R;
import razerdp.friendcircle.utils.UIHelper;

/**
 * Created by 大灯泡 on 2016/2/19.
 * 点击展开更多
 */
public class ClickShowMoreLayout extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "ClickShowMoreLayout";
    private TextView mTextView;
    private TextView mClickToShow;

    private int textColor;
    private int textSize;

    private int showLine;
    private String clickText;

    private boolean hasMore;
    private boolean hasGetLineCount=false;

    public ClickShowMoreLayout(Context context) {
        this(context,null);
    }

    public ClickShowMoreLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClickShowMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.ClickShowMoreLayout);
        textColor=a.getColor(R.styleable.ClickShowMoreLayout_text_color,0xff1a1a1a);
        textSize=a.getDimensionPixelSize(R.styleable.ClickShowMoreLayout_text_size,14);
        showLine=a.getInt(R.styleable.ClickShowMoreLayout_show_line,8);
        clickText=a.getString(R.styleable.ClickShowMoreLayout_click_text);
        if (TextUtils.isEmpty(clickText))clickText="全文";
        a.recycle();

        initView(context);

    }

    private void initView(Context context) {
        mTextView=new TextView(context);
        mClickToShow=new TextView(context);

        mTextView.setTextSize(textSize);
        mTextView.setTextColor(textColor);
        mTextView.setMaxLines(showLine);

        mClickToShow.setTextSize(textSize);
        mClickToShow.setTextColor(getResources().getColor(R.color.nick));
        mClickToShow.setText(clickText);

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin= UIHelper.dipToPx(context,10f);
        mClickToShow.setLayoutParams(params);
        mClickToShow.setOnClickListener(this);

        setOrientation(VERTICAL);
        addView(mTextView);
        addView(mClickToShow);
    }

    @Override
    public void onClick(View v) {
        if (((TextView)v).getText().toString().equals(clickText)){
            mTextView.setMaxLines(Integer.MAX_VALUE);
            mClickToShow.setText("收起");
        }else {
            mTextView.setMaxLines(showLine);
            mClickToShow.setText(clickText);
        }
    }

    public void setText(String str){
        mTextView.setText(str);
        mTextView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!hasGetLineCount) {
                    hasMore = mTextView.getLineCount() > showLine;
                    hasGetLineCount=true;
                }
                mClickToShow.setVisibility(hasMore?VISIBLE:GONE);
                mTextView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }
    public CharSequence getText(){
        return mTextView.getText();
    }
}

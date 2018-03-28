package razerdp.github.com.ui.widget.commentwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import razerdp.github.com.ui.widget.span.ClickableSpanEx;
import razerdp.github.com.ui.widget.span.SpannableStringBuilderCompat;

/**
 * Created by 大灯泡 on 2016/2/23.
 * 评论控件
 */
public class CommentWidget extends TextView {
    private static final String TAG = "CommentWidget";
    //用户名颜色
    private int textColor = 0xff517fae;
    private static final int textSize = 14;
    SpannableStringBuilderCompat mSpannableStringBuilderCompat;
    private OnCommentUserClickListener mOnCommentUserClickListener;

    public CommentWidget(Context context) {
        this(context, null);
    }

    public CommentWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMovementMethod(LinkMovementMethod.getInstance());
        setOnTouchListener(new ClickableSpanEx.ClickableSpanSelector());
        this.setHighlightColor(0x00000000);
        setTextSize(textSize);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommentWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setMovementMethod(LinkMovementMethod.getInstance());
        this.setHighlightColor(0x00000000);
        setTextSize(textSize);
    }

    public void setCommentText(IComment info) {
        if (info == null) return;
        try {
            setTag(info);
            createCommentStringBuilder(info);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(TAG, "虽然在下觉得不可能会有这个情况，但还是捕捉下吧，万一被打脸呢。。。");
        }
    }

    private void createCommentStringBuilder(@NonNull IComment info) {
        if (mSpannableStringBuilderCompat == null) {
            mSpannableStringBuilderCompat = new SpannableStringBuilderCompat();
        } else {
            mSpannableStringBuilderCompat.clear();
            mSpannableStringBuilderCompat.clearSpans();
        }
        String content = ": " + info.getCommentContent() + "\0";
        boolean isApply = !TextUtils.isEmpty(info.getReplyerName());

        if (!isApply) {
            //用户A，B不空，证明是回复评论
            CommentClick userA = new CommentClick.Builder(getContext(), info)
                    .setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(textSize)
                    .setClickListener(mOnCommentUserClickListener)
                    .build();
            mSpannableStringBuilderCompat.append(info.getCommentCreatorName(), userA, 0);
            mSpannableStringBuilderCompat.append(content);
        } else {
            //如果是一条回复评论
            CommentClick userA = new CommentClick.Builder(getContext(), info).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(textSize)
                    .setClickListener(mOnCommentUserClickListener)
                    .build();
            mSpannableStringBuilderCompat.append(info.getCommentCreatorName(), userA, 0);
            mSpannableStringBuilderCompat.append(" 回复 ");

            CommentClick userB = new CommentClick.Builder(getContext(), info).setColor(0xff517fae)
                    .setClickEventColor(0xffc6c6c6)
                    .setTextSize(textSize)
                    .setClickListener(mOnCommentUserClickListener)
                    .build();
            mSpannableStringBuilderCompat.append(info.getReplyerName(), userB, 0);
            mSpannableStringBuilderCompat.append(content);
        }
        setText(mSpannableStringBuilderCompat);
    }

    public IComment getData() throws ClassCastException {
        return (IComment) getTag();
    }

    public OnCommentUserClickListener getOnCommentUserClickListener() {
        return mOnCommentUserClickListener;
    }

    public void setOnCommentUserClickListener(OnCommentUserClickListener onCommentUserClickListener) {
        mOnCommentUserClickListener = onCommentUserClickListener;
        if (mSpannableStringBuilderCompat != null) {
            CommentClick[] spans = mSpannableStringBuilderCompat.getSpans(0, mSpannableStringBuilderCompat.length(), CommentClick.class);
            if (spans != null && spans.length > 0) {
                for (CommentClick span : spans) {
                    span.setOnCommentUserClickListener(mOnCommentUserClickListener);
                }
            }
        }
    }

}

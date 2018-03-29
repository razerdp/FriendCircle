package razerdp.github.com.ui.widget.commentwidget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.view.View;

import razerdp.github.com.ui.util.UIHelper;
import razerdp.github.com.ui.widget.span.ClickableSpanEx;

/**
 * Created by 大灯泡 on 2016/2/23.
 * 评论点击事件
 */
public class CommentClick extends ClickableSpanEx {
    private Context mContext;
    private int textSize;
    private IComment mComment;
    private OnCommentUserClickListener mOnCommentUserClickListener;

    private CommentClick() {
    }

    private CommentClick(Builder builder) {
        super(builder.color, builder.clickEventColor);
        mContext = builder.mContext;
        mComment = builder.mComment;
        mOnCommentUserClickListener = builder.mListener;
        textSize = builder.textSize;
    }

    @Override
    public void onClickEx(View widget, CharSequence text) {
        if (mComment != null && mOnCommentUserClickListener != null) {
            mOnCommentUserClickListener.onCommentClicked(mComment, text);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setTextSize(textSize);
        ds.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setOnCommentUserClickListener(OnCommentUserClickListener listener) {
        this.mOnCommentUserClickListener = listener;
    }

    public static class Builder {
        private int color;
        private Context mContext;
        private int textSize = 16;
        private IComment mComment;
        private int clickEventColor;
        private OnCommentUserClickListener mListener;

        public Builder(Context context, @NonNull IComment comment) {
            mContext = context;
            mComment = comment;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = UIHelper.sp2px(textSize);
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setClickEventColor(int color) {
            this.clickEventColor = color;
            return this;
        }

        public Builder setClickListener(OnCommentUserClickListener mListener) {
            this.mListener = mListener;
            return this;
        }

        public CommentClick build() {
            return new CommentClick(this);
        }
    }
}

package razerdp.friendcircle.widget.commentwidget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import razerdp.friendcircle.api.data.model.UserInfo;

/**
 * Created by 大灯泡 on 2016/2/23.
 * 评论点击事件
 */
public class CommentClick extends ClickableSpan {
    private static final int DEFAULT_COLOR = 0xff517fae;
    private int color;
    private Context mContext;
    private int textSize;
    private UserInfo mUserInfo;

    private CommentClick() {}

    private CommentClick(Builder builder) {
        mContext = builder.mContext;
        mUserInfo = builder.mUserInfo;
        this.textSize = builder.textSize;
        this.color = builder.color;
    }

    @Override
    public void onClick(View widget) {
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        //去掉下划线
        if (color == 0) {
            ds.setColor(DEFAULT_COLOR);
        }
        else {
            ds.setColor(color);
        }
        ds.setTextSize(textSize);
        ds.setUnderlineText(false);
    }

    public static class Builder {
        private int color;
        private Context mContext;
        private int textSize=14;
        private UserInfo mUserInfo;

        public Builder(Context context, @NonNull UserInfo info) {
            mContext = context;
            mUserInfo=info;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public CommentClick build() {
            return new CommentClick(this);
        }
    }
}

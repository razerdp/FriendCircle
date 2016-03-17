package razerdp.friendcircle.widget.praisewidget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;
import razerdp.friendcircle.app.mvp.model.entity.UserInfo;
import razerdp.friendcircle.utils.UIHelper;

/**
 * Created by 大灯泡 on 2016/2/21.
 * 点击事件
 */
public class PraiseClick extends ClickableSpan {
    private static final int DEFAULT_COLOR = 0xff517fae;
    private int color;
    private Context mContext;
    private int textSize;
    private UserInfo mPraiseInfo;

    private PraiseClick() {}

    private PraiseClick(Builder builder) {
        mContext = builder.mContext;
        mPraiseInfo = builder.mPraiseInfo;
        this.textSize = builder.textSize;
        this.color = builder.color;
    }

    @Override
    public void onClick(View widget) {
        if (mPraiseInfo!=null)
        Toast.makeText(mContext, "当前用户名是： " + mPraiseInfo.nick + "   它的ID是： " + mPraiseInfo.userId,
                Toast.LENGTH_SHORT).show();
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
        private int textSize=16;
        private UserInfo mPraiseInfo;

        public Builder(Context context, @NonNull UserInfo info) {
            mContext = context;
            mPraiseInfo=info;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = UIHelper.sp2px(mContext,textSize);
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public PraiseClick build() {
            return new PraiseClick(this);
        }
    }
}

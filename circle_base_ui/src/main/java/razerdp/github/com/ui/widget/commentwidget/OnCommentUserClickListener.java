package razerdp.github.com.ui.widget.commentwidget;

import android.support.annotation.NonNull;

/**
 * Created by 大灯泡 on 2017/11/17.
 * <p>
 * 评论控件点击
 */

public interface OnCommentUserClickListener {
    void onCommentClicked(@NonNull IComment comment,CharSequence text);
}

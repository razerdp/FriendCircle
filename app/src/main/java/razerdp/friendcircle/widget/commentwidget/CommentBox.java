package razerdp.friendcircle.widget.commentwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import razerdp.friendcircle.R;
import razerdp.friendcircle.utils.StringUtil;
import razerdp.friendcircle.utils.UIHelper;

/**
 * Created by 大灯泡 on 2016/12/8.
 * <p>
 * 评论输入框
 */

public class CommentBox extends FrameLayout {

    private EditText mInputContent;
    private TextView mSend;
    private OnSendClickListener onSendClickListener;

    private boolean isShowing;

    //草稿
    private String draftString;


    public CommentBox(Context context) {
        this(context, null);
    }

    public CommentBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_comment_box, this);
        mInputContent = (EditText) findViewById(R.id.ed_comment_content);
        mSend = (TextView) findViewById(R.id.btn_send);
        mSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSendClickListener != null) onSendClickListener.onSendClick(v);
            }
        });
        setVisibility(GONE);
    }


    // TODO: 2016/12/8 考虑回复评论的情况
    public void showCommentBox() {
        if (isShowing) return;
        this.isShowing = true;
        setVisibility(VISIBLE);
        // TODO: 2016/12/8 如果有多窗口，则需要考虑草稿的问题.
        UIHelper.showInputMethod(mInputContent, 150);
    }

    public void dismissCommentBox() {
        if (!isShowing) return;
        this.isShowing = false;
        UIHelper.hideInputMethod(mInputContent);
        setVisibility(GONE);
    }

    public void toggleCommentBox() {
        if (isShowing) {
            dismissCommentBox();
        } else {
            showCommentBox();
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void clearDraft() {
        this.draftString = null;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismissCommentBox();
    }

    public OnSendClickListener getOnSendClickListener() {
        return onSendClickListener;
    }

    public void setOnSendClickListener(OnSendClickListener onSendClickListener) {
        this.onSendClickListener = onSendClickListener;
    }

    public interface OnSendClickListener {
        void onSendClick(View v);
    }
}

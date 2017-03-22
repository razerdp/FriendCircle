package razerdp.friendcircle.ui.widget.commentwidget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import razerdp.friendcircle.R;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baselibrary.utils.StringUtil;

/**
 * Created by 大灯泡 on 2016/12/8.
 * <p>
 * 评论输入框
 */

// FIXME: 2016/12/13 跟别的控件耦合度较高，后期考虑优化
public class CommentBox extends FrameLayout {

    private EditText mInputContent;
    private TextView mSend;
    private OnCommentSendClickListener onCommentSendClickListener;

    private boolean isShowing;

    //草稿
    private String draftString;

    private CommentInfo commentInfo;
    private String momentid;
    private int dataPos;
    private CommentWidget commentWidget;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CommentType.TYPE_CREATE, CommentType.TYPE_REPLY})
    public @interface CommentType {
        //评论
        int TYPE_CREATE = 0x10;
        //回复
        int TYPE_REPLY = 0x11;
    }


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
                if (onCommentSendClickListener != null)
                    onCommentSendClickListener.onCommentSendClick(v,
                                                                  momentid,
                                                                  commentInfo == null ? null : commentInfo.getAuthor().getUserid(),
                                                                  mInputContent.getText().toString().trim());
            }
        });
        setVisibility(GONE);
    }

    public void showCommentBox(@NonNull String momentid, @Nullable CommentInfo commentInfo) {
        if (TextUtils.isEmpty(momentid)) return;
        if (isShowing) return;
        this.isShowing = true;
        this.commentInfo = commentInfo;
        //对不同的回复动作执行不同的
        if (this.commentInfo != null) {
            mInputContent.setHint("回复 " + commentInfo.getAuthor().getNick() + ":");
        } else {
            mInputContent.setHint("评论");
        }
        //对于同一条动态恢复草稿，否则不恢复
        if (TextUtils.equals(momentid, this.momentid) && StringUtil.noEmpty(draftString)) {
            mInputContent.setText(draftString);
            mInputContent.setSelection(draftString.length());
        } else {
            mInputContent.setText(null);
        }
        setMomentid(momentid);
        setVisibility(VISIBLE);
        UIHelper.showInputMethod(mInputContent, 150);
    }

    public void dismissCommentBox(boolean clearDraft) {
        if (!isShowing) return;
        this.isShowing = false;
        if (!clearDraft) {
            this.draftString = mInputContent.getText().toString().trim();
        } else {
            clearDraft();
        }
        UIHelper.hideInputMethod(mInputContent);
        setVisibility(GONE);
    }

    /**
     * 切换评论框的状态
     *
     * @param momentid
     * @param commentInfo
     * @param clearDraft  是否清除草稿
     */
    public void toggleCommentBox(@NonNull String momentid, @Nullable CommentInfo commentInfo, boolean clearDraft) {
        if (isShowing) {
            dismissCommentBox(clearDraft);
        } else {
            showCommentBox(momentid, commentInfo);
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void clearDraft() {
        this.draftString = null;
    }


    public String getMomentid() {
        return momentid;
    }

    public void setMomentid(String momentid) {
        this.momentid = momentid;
    }

    public CommentInfo getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(CommentInfo commentInfo) {
        this.commentInfo = commentInfo;
    }

    public boolean isReply() {
        return commentInfo != null;
    }

    public int getDataPos() {
        return dataPos;
    }

    public void setDataPos(int dataPos) {
        this.dataPos = dataPos;
    }

    @CommentType
    public int getCommentType() {
        return commentInfo == null ? CommentType.TYPE_CREATE : CommentType.TYPE_REPLY;
    }

    public CommentWidget getCommentWidget() {
        return commentWidget;
    }

    public void setCommentWidget(CommentWidget commentWidget) {
        this.commentWidget = commentWidget;
    }

    @Override
    protected void onDetachedFromWindow() {
        dismissCommentBox(true);
        super.onDetachedFromWindow();
    }

    public OnCommentSendClickListener getOnCommentSendClickListener() {
        return onCommentSendClickListener;
    }

    public void setOnCommentSendClickListener(OnCommentSendClickListener onCommentSendClickListener) {
        this.onCommentSendClickListener = onCommentSendClickListener;
    }

    public interface OnCommentSendClickListener {
        void onCommentSendClick(View v, String momentid, String commentAuthorId, String commentContent);
    }
}

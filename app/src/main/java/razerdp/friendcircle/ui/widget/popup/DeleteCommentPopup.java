package razerdp.friendcircle.ui.widget.popup;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.R;
import razerdp.friendcircle.app.mvp.model.entity.CommentInfo;

/**
 * Created by 大灯泡 on 2016/4/22.
 * 删除评论的popup
 */
public class DeleteCommentPopup extends BasePopupWindow implements View.OnClickListener {

    private TextView mDel;
    private TextView mCancel;
    private CommentInfo commentInfo;

    public DeleteCommentPopup(Activity context) {
        super(context);

        mDel = (TextView) findViewById(R.id.delete);
        mCancel = (TextView) findViewById(R.id.cancel);

        setViewClickListener(this, mDel, mCancel);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_container);
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(300, 0, 300);
    }

    @Override
    protected Animation initExitAnimation() {
        return getTranslateAnimation(0, 300, 300);
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_delete_comment);
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }


    public void showPopupWindow(CommentInfo commentInfo) {
        if (commentInfo==null)return;
        this.commentInfo=commentInfo;
        super.showPopupWindow();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                if (mDeleteCommentClickListener != null) {
                    mDeleteCommentClickListener.onDelClick(commentInfo);
                }
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private OnDeleteCommentClickListener mDeleteCommentClickListener;

    public OnDeleteCommentClickListener getOnDeleteCommentClickListener() {
        return mDeleteCommentClickListener;
    }

    public void setOnDeleteCommentClickListener(OnDeleteCommentClickListener deleteCommentClickListener) {
        mDeleteCommentClickListener = deleteCommentClickListener;
    }

    public interface OnDeleteCommentClickListener {
        void onDelClick(CommentInfo commentInfo);
    }
}

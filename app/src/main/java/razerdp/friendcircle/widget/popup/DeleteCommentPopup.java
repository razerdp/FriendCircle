package razerdp.friendcircle.widget.popup;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.R;

/**
 * Created by 大灯泡 on 2016/4/22.
 * 删除评论的popup
 */
public class DeleteCommentPopup extends BasePopupWindow implements View.OnClickListener{

    private TextView mDel;
    private TextView mCancel;

    public DeleteCommentPopup(Activity context) {
        super(context);

        mDel= (TextView) findViewById(R.id.delete);
        mCancel= (TextView) findViewById(R.id.cancel);

        setViewClickListener(this,mDel,mCancel);
    }

    @Override
    protected Animation getShowAnimation() {
        return getTranslateAnimation(300,0,300);
    }

    @Override
    public Animation getExitAnimation() {
        return getTranslateAnimation(0,300,300);
    }

    @Override
    protected View getClickToDismissView() {
        return mPopupView;
    }

    @Override
    public View getPopupView() {
        return getPopupViewById(R.layout.popup_delete_comment);
    }

    @Override
    public View getAnimaView() {
        return mPopupView.findViewById(R.id.popup_container);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:
                if (mDeleteCommentClickListener!=null)
                    mDeleteCommentClickListener.onDelClick(v);
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

    public interface OnDeleteCommentClickListener{
        void onDelClick(View v);
    }
}

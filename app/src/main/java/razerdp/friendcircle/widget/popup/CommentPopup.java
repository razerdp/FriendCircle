package razerdp.friendcircle.widget.popup;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import razerdp.friendcircle.api.data.entity.DynamicInfo;
import razerdp.friendcircle.widget.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/3/6.
 * 朋友圈点赞
 */
public class CommentPopup extends BasePopupWindow {
    private static final String TAG = "CommentPopup";

    private ImageView mLikeView;
    private TextView mLikeText;

    private RelativeLayout mLikeClikcLayout;
    private RelativeLayout mCommentClickLayout;

    private DynamicInfo mDynamicInfo;

    private int[] viewLocation;

    private WeakHandler handler;


    public CommentPopup(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected Animation getShowAnimation() {
        return null;
    }

    @Override
    protected View getClickToDismissView() {
        return null;
    }

    @Override
    public View getPopupView() {
        return null;
    }

    @Override
    public View getAnimaView() {
        return null;
    }


    static class WeakHandler extends Handler{
        private final WeakReference<Context> contenxt;

        public WeakHandler(Context contenxt) {
            this.contenxt = new WeakReference<Context>(contenxt);
        }
    }
}

package razerdp.friendcircle.ui.widget.popup;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.razerdp.github.com.common.entity.LikesInfo;
import com.razerdp.github.com.common.entity.MomentsInfo;
import com.razerdp.github.com.common.manager.LocalHostManager;

import razerdp.basepopup.BasePopupWindow;
import razerdp.friendcircle.R;
import razerdp.github.com.lib.thirdpart.WeakHandler;
import razerdp.github.com.lib.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2016/3/6.
 * 朋友圈点赞
 */
public class CommentPopup extends BasePopupWindow implements View.OnClickListener {
    private static final String TAG = "CommentPopup";

    private ImageView mLikeView;
    private TextView mLikeText;

    private RelativeLayout mLikeClikcLayout;
    private RelativeLayout mCommentClickLayout;

    private MomentsInfo mMomentsInfo;

    private WeakHandler handler;
    private ScaleAnimation mScaleAnimation;

    private OnCommentPopupClickListener mOnCommentPopupClickListener;

    //是否已经点赞
    private boolean hasLiked;

    public CommentPopup(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        handler = new WeakHandler();

        mLikeView = (ImageView) findViewById(R.id.iv_like);
        mLikeText = (TextView) findViewById(R.id.tv_like);

        mLikeClikcLayout = (RelativeLayout) findViewById(R.id.item_like);
        mCommentClickLayout = (RelativeLayout) findViewById(R.id.item_comment);

        mLikeClikcLayout.setOnClickListener(this);
        mCommentClickLayout.setOnClickListener(this);

        buildAnima();
        setBlurBackgroundEnable(true);
        setAllowInterceptTouchEvent(false);
        setAllowDismissWhenTouchOutside(true);
        setPopupFadeEnable(false);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        TranslateAnimation showAnima = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                1f,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0);
        showAnima.setInterpolator(new DecelerateInterpolator());
        showAnima.setDuration(250);
        showAnima.setFillAfter(true);
        return showAnima;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        TranslateAnimation showAnima = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                1f,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0);
        showAnima.setInterpolator(new DecelerateInterpolator());
        showAnima.setDuration(250);
        showAnima.setFillAfter(true);
        return showAnima;
    }

    private void buildAnima() {
        mScaleAnimation = new ScaleAnimation(1f, 2.5f, 1f, 2.5f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(300);
        mScaleAnimation.setInterpolator(new SpringInterPolator());
        mScaleAnimation.setFillAfter(false);

        mScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 150);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_comment);
    }


    @Override
    public void showPopupWindow(View v) {
        setOffsetX(-getWidth() - 10);
        setOffsetY(-getHeight() * 2 / 3);
        super.showPopupWindow(v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_like:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onLikeClick(v, mMomentsInfo, hasLiked);
                    mLikeView.clearAnimation();
                    mLikeView.startAnimation(mScaleAnimation);
                }
                break;
            case R.id.item_comment:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onCommentClick(v, mMomentsInfo);
                    dismissWithOutAnimate();
                }
                break;
        }
    }
    //=============================================================Getter/Setter

    public OnCommentPopupClickListener getOnCommentPopupClickListener() {
        return mOnCommentPopupClickListener;
    }

    public void setOnCommentPopupClickListener(OnCommentPopupClickListener onCommentPopupClickListener) {
        mOnCommentPopupClickListener = onCommentPopupClickListener;
    }


    public void updateMomentInfo(@NonNull MomentsInfo info) {
        this.mMomentsInfo = info;
        hasLiked = false;
        if (!ToolUtil.isListEmpty(info.getLikesList())) {
            for (LikesInfo likesInfo : info.getLikesList()) {
                if (TextUtils.equals(likesInfo.getUserid(), LocalHostManager.INSTANCE.getUserid())) {
                    hasLiked = true;
                    break;
                }
            }
        }
        mLikeText.setText(hasLiked ? "取消" : "赞");

    }

    //=============================================================InterFace
    public interface OnCommentPopupClickListener {
        void onLikeClick(View v, @NonNull MomentsInfo info, boolean hasLiked);

        void onCommentClick(View v, @NonNull MomentsInfo info);
    }

    static class SpringInterPolator extends LinearInterpolator {

        public SpringInterPolator() {
        }


        @Override
        public float getInterpolation(float input) {
            return (float) Math.sin(input * Math.PI);
        }
    }
}
